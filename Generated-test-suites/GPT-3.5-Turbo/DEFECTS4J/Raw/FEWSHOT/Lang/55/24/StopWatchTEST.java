import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

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

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.stop();
    }

    @Test
    public void testStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopwatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        stopwatch.stop();
    }

    @Test
    public void testReset() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        stopwatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopwatch.runningState);
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.splitState);
        assertEquals(-1, stopwatch.startTime);
        assertEquals(-1, stopwatch.stopTime);
    }

    @Test
    public void testSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopwatch.splitState);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.unsplit();
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
    public void testSuspendWithoutStart() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.suspend();
    }

    @Test
    public void testSuspend() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopwatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.resume();
    }

    @Test
    public void testResume() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.runningState);
    }

    @Test
    public void testGetTime() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        assertEquals(0, stopwatch.getTime());
        stopwatch.stop();
        assertEquals(stopwatch.stopTime - stopwatch.startTime, stopwatch.getTime());
    }

    @Test(expected = RuntimeException.class)
    public void testIllegalRunningState() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.runningState = -1;
        stopwatch.getTime();
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        assertEquals(stopwatch.stopTime - stopwatch.startTime, stopwatch.getSplitTime());
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
        assertEquals("00:00:00.000", stopwatch.toString());
        stopwatch.stop();
        assertEquals(DurationFormatUtils.formatDurationHMS(stopwatch.getTime()), stopwatch.toString());
    }

    @Test
    public void testToSplitString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        assertEquals(DurationFormatUtils.formatDurationHMS(stopwatch.getSplitTime()), stopwatch.toSplitString());
    }
}