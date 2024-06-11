import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopwatch = new StopWatch();
        
        stopwatch.start();
        
        // Check if the stopwatch is running
        assertEquals(true, stopwatch.isRunning());
    }

    @Test
    public void testStop() {
        StopWatch stopwatch = new StopWatch();
        
        stopwatch.start();
        stopwatch.stop();
        
        // Check if the stopwatch is stopped
        assertEquals(false, stopwatch.isRunning());
    }

    @Test
    public void testReset() {
        StopWatch stopwatch = new StopWatch();
        
        stopwatch.start();
        stopwatch.stop();
        stopwatch.reset();
        
        // Check if the stopwatch is reset
        assertEquals(false, stopwatch.isRunning());
        assertEquals(false, stopwatch.isStarted());
        assertEquals(false, stopwatch.isStopped());
    }

    @Test
    public void testSplit() {
        StopWatch stopwatch = new StopWatch();
        
        stopwatch.start();
        stopwatch.split();
        
        // Check if the stopwatch is split
        assertEquals(true, stopwatch.isSplit());
    }

    @Test
    public void testUnsplit() {
        StopWatch stopwatch = new StopWatch();
        
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        
        // Check if the stopwatch is unsplit
        assertEquals(false, stopwatch.isSplit());
    }

    @Test
    public void testSuspend() {
        StopWatch stopwatch = new StopWatch();
        
        stopwatch.start();
        stopwatch.suspend();
        
        // Check if the stopwatch is suspended
        assertEquals(true, stopwatch.isSuspended());
    }

    @Test
    public void testResume() {
        StopWatch stopwatch = new StopWatch();
        
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        
        // Check if the stopwatch is resumed
        assertEquals(false, stopwatch.isSuspended());
    }

    @Test
    public void testGetTime() {
        StopWatch stopwatch = new StopWatch();
        
        stopwatch.start();
        long time1 = stopwatch.getTime();
        
        // Sleep for 2 seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        stopwatch.stop();
        long time2 = stopwatch.getTime();
        
        // Check if the time is accurate
        assertTrue(time2 >= time1);
        assertTrue(time2 > 0);
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopwatch = new StopWatch();
        
        stopwatch.start();
        stopwatch.split();
        long splitTime = stopwatch.getSplitTime();
        
        // Check if the split time is accurate
        assertTrue(splitTime > 0);
    }
}