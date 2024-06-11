import org.junit.Test;

import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue("Stopwatch should be running after start", stopWatch.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertFalse("Stopwatch should not be running after stop", stopWatch.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        assertFalse("Stopwatch should not be running after reset", stopWatch.isRunning());
        assertEquals("Stopwatch time should be reset to 0", 0, stopWatch.getTime());
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue("Stopwatch should be split after calling split", stopWatch.isSplit());
        assertEquals("Split time should be positive", true, stopWatch.getSplitTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse("Stopwatch should not be split after calling unsplit", stopWatch.isSplit());
        assertEquals("Split time should be reset to 0 after calling unsplit", 0, stopWatch.getSplitTime());
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertEquals("Stopwatch state should be suspended", StopWatch.STATE_SUSPENDED, stopWatch.getState());
        assertEquals("Stopwatch time should not include suspended time", 0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.resume();
    }

    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals("Stopwatch state should be running after resuming", StopWatch.STATE_RUNNING, stopWatch.getState());
        assertTrue("Stopwatch time should include resumed time", stopWatch.getTime() > 0);
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue("Stopwatch time should be positive", stopWatch.getTime() >= 0);
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String timeString = stopWatch.toString();
        assertNotNull("Stopwatch time should not be null", timeString);
        assertFalse("Stopwatch time string should not be empty", timeString.isEmpty());
    }

    private class StopWatch extends org.apache.commons.lang.time.StopWatch {
        public int getState() {
            return runningState;
        }

        public boolean isRunning() {
            return runningState == STATE_RUNNING;
        }

        public boolean isSplit() {
            return splitState == STATE_SPLIT;
        }
    }
}