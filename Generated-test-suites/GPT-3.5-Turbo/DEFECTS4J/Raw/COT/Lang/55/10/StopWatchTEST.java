import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.start();
    }

    @Test
    public void testStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopwatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopNotRunning() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.stop();
    }

    @Test
    public void testReset() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        stopwatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopwatch.runningState);
    }

    @Test
    public void testSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopwatch.splitState);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitNotRunning() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.split();
    }

    @Test
    public void testUnsplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.splitState);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitNotSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.unsplit();
    }

    @Test
    public void testSuspend() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopwatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendNotRunning() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.suspend();
    }

    @Test
    public void testResume() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeNotSuspended() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.resume();
    }

    @Test
    public void testGetTime() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        long time = stopwatch.getTime();
        assertTrue(time >= 0);
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        long splitTime = stopwatch.getSplitTime();
        assertTrue(splitTime >= 0);
    }

    @Test
    public void testToString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        String timeString = stopwatch.toString();
        assertNotNull(timeString);
    }

    @Test
    public void testToSplitString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        String splitTimeString = stopwatch.toSplitString();
        assertNotNull(splitTimeString);
    }
}