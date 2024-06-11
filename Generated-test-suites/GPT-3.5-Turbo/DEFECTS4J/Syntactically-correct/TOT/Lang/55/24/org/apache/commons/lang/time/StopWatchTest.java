import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang.time.StopWatch;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    // Testing start() method
    @Test
    public void testStart() {
        // Typical use case
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        
        // Error scenario: calling start() twice without calling reset()
        try {
            stopWatch.start();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
        
        // Edge case: calling start() after calling stop()
        stopWatch.reset();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    // Testing stop() method
    @Test
    public void testStop() {
        // Typical use case
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        
        // Error scenario: calling stop() without calling start() or suspend()
        try {
            stopWatch.stop();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
        
        // Edge case: calling stop() twice
        stopWatch.reset();
        stopWatch.start();
        stopWatch.stop();
        try {
            stopWatch.stop();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
    }

    // Testing reset() method
    @Test
    public void testReset() {
        // Typical use case
        stopWatch.start();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        
        // Edge case: calling reset() twice
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    // Testing split() method
    @Test
    public void testSplit() {
        // Typical use case
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        
        // Error scenario: calling split() without calling start()
        stopWatch.reset();
        try {
            stopWatch.split();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
        
        // Edge case: calling split() twice
        stopWatch.reset();
        stopWatch.start();
        stopWatch.split();
        try {
            stopWatch.split();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
    }

    // Testing unsplit() method
    @Test
    public void testUnsplit() {
        // Typical use case
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        
        // Error scenario: calling unsplit() without calling split()
        stopWatch.reset();
        try {
            stopWatch.unsplit();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
        
        // Edge case: calling unsplit() twice
        stopWatch.reset();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        try {
            stopWatch.unsplit();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
    }

    // Testing suspend() method
    @Test
    public void testSuspend() {
        // Typical use case
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        
        // Error scenario: calling suspend() without calling start()
        stopWatch.reset();
        try {
            stopWatch.suspend();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
        
        // Edge case: calling suspend() twice
        stopWatch.reset();
        stopWatch.start();
        stopWatch.suspend();
        try {
            stopWatch.suspend();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
    }

    // Testing resume() method
    @Test
    public void testResume() {
        // Typical use case
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        
        // Error scenario: calling resume() without calling suspend()
        stopWatch.reset();
        try {
            stopWatch.resume();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
        
        // Edge case: calling resume() twice
        stopWatch.reset();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        try {
            stopWatch.resume();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
    }

    // Testing getTime() method
    @Test
    public void testGetTime() {
        // Typical use case: running the stopwatch for a specific time
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertTrue(time >= 1000 && time <= 1100);
        
        // Typical use case: getting the time without starting the stopwatch
        stopWatch.reset();
        time = stopWatch.getTime();
        assertEquals(0, time);
        
        // Edge case: getting the time after starting the stopwatch but before stopping it
        stopWatch.reset();
        stopWatch.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        time = stopWatch.getTime();
        assertTrue(time >= 500 && time <= 600);
    }

    // Testing getSplitTime() method
    @Test
    public void testGetSplitTime() {
        // Typical use case
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime >= 1000 && splitTime <= 1100);
        
        // Error scenario: calling getSplitTime() without calling split()
        stopWatch.reset();
        try {
            splitTime = stopWatch.getSplitTime();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
    }

    // Testing toString() method
    @Test
    public void testToString() {
        // Typical use case: running the stopwatch for a specific time
        stopWatch.start();
        try {
            Thread.sleep(1234);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        stopWatch.stop();
        String timeString = stopWatch.toString();
        assertEquals("0:00:01.234", timeString);
        
        // Typical use case: getting the time without starting the stopwatch
        stopWatch.reset();
        timeString = stopWatch.toString();
        assertEquals("0:00:00.000", timeString);
        
        // Edge case: getting the time after starting the stopwatch but before stopping it
        stopWatch.reset();
        stopWatch.start();
        try {
            Thread.sleep(567);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        timeString = stopWatch.toString();
        assertEquals("0:00:00.567", timeString);
    }

    // Testing toSplitString() method
    @Test
    public void testToSplitString() {
        // Typical use case: running the stopwatch for a specific time and splitting it
        stopWatch.start();
        try {
            Thread.sleep(1234);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        assertEquals("0:00:01.234", splitTimeString);
        
        // Error scenario: calling toSplitString() without calling split()
        stopWatch.reset();
        try {
            splitTimeString = stopWatch.toSplitString();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
    }

}