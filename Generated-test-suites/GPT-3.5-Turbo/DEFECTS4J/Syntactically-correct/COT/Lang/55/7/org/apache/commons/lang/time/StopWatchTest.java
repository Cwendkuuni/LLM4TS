
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        assertEquals(stopwatch.getTime(), 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.start();
    }

    @Test
    public void testStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        assertTrue(stopwatch.getTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.stop();
    }

    @Test
    public void testReset() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        stopwatch.reset();
        assertEquals(stopwatch.getTime(), 0);
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        assertTrue(stopwatch.getSplitTime() > 0);
        stopwatch.unsplit();
        assertEquals(stopwatch.getSplitTime(), 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.unsplit();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        long timeBeforeResume = stopwatch.getTime();
        stopwatch.resume();
        assertTrue(stopwatch.getTime() > timeBeforeResume);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.resume();
    }

    @Test
    public void testGetTime() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 1000) {
            // Wait for 1 second
        }
        stopwatch.stop();
        assertTrue(stopwatch.getTime() >= 1000);
        assertTrue(stopwatch.getTime() < 1100);
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        long splitTime = stopwatch.getSplitTime();
        stopwatch.stop();
        assertEquals(stopwatch.getSplitTime(), splitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.getSplitTime();
    }

    @Test
    public void testToString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 1000) {
            // Wait for 1 second
        }
        stopwatch.stop();
        assertEquals(stopwatch.toString(), "0:0:1.0");
    }

    @Test
    public void testToSplitString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        assertEquals(stopwatch.toSplitString(), "0:0:0.0");
    }
}

