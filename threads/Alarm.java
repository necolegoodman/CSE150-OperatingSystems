package nachos.threads;

import java.util.PriorityQueue;

import nachos.machine.*;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
    /**
     * Allocate a new Alarm. Set the machine's timer interrupt handler to this
     * alarm's callback.
     *
     * <p><b>Note</b>: Nachos will not function correctly with more than one
     * alarm.
     */
    public Alarm() {
	Machine.timer().setInterruptHandler(new Runnable() {
		public void run() { timerInterrupt(); }
	    });
    }
// Initialize priority queue to store sleeping threads
   public PriorityQueue<KThread/*Timer*/> sleepingThreads = new PriorityQueue<KThread/*Timer*/>();//double check for error
    /**
     * The timer interrupt handler. This is called by the machine's timer
     * periodically (approximately every 500 clock ticks). Causes the current
     * thread to yield, forcing a context switch if there is another thread
     * that should be run.
     */
    public void timerInterrupt() {
	//KThread.currentThread().yield(); 
    	
	Machine.interrupt().disable();
	
	ThreadTime rand = sleepingThreads.peek(); 
	
	while (rand != null && rand.getWaitTime() < Machine.timer().getTime()){
		rand.remove();
		rand.getThread();
		rand.ready();
	}

	
	Machine.interrupt().enable();
	KThread.currentThread();
	KThread.yield(); //Thread is yielding in the queue meaning it is null
	

    }

    /**
     * Put the current thread to sleep for at least <i>x</i> ticks,
     * waking it up in the timer interrupt handler. The thread must be
     * woken up (placed in the scheduler ready set) during the first timer
     * interrupt where
     *
     * <p><blockquote>
     * (current time) >= (WaitUntil called time)+(x)
     * </blockquote>
     *
     * @param	x	the minimum number of clock ticks to wait.
     *
     * @see	nachos.machine.Timer#getTime()
     */
    public void waitUntil(long x) {
	// for now, cheat just to get something working (busy waiting is bad)
	long wakeTime = Machine.timer().getTime() + x;
	
	while (wakeTime > Machine.timer().getTime()){
	    KThread.yield();
	    KThread currentThreadTimer = new threadTimer(KThread.currentThread, wakeTime());
		
	Machine.interrupt().disable();
	
	sleepingThreads.add(currentThreadTimer);
	//if(KThread.CurrentThread() == wakeTime){
	//KThread currentThread.wakeTime = waitingQueue;
		KThread.sleep();
	}
	
	Machine.interrupt().enable();
		KThread.yield();
    }
}

