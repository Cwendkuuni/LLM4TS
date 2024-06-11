import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {
    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertNotNull(stopWatch.getStartTime());
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertNotNull(stopWatch.getStopTime());
    }

    @Test
    public void testReset() {
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNull(stopWatch.getStartTime());
        assertNull(stopWatch.getStopTime());
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        assertNotNull(stopWatch.getStopTime());
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testGetTimeStopWatchStopped() {
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertEquals(stopWatch.getStopTime() - stopWatch.getStartTime(), time);
    }

    @Test
    public void testGetTimeStopWatchSuspended() {
        stopWatch.start();
        stopWatch.suspend();
        long time = stopWatch.getTime();
        assertEquals(stopWatch.getStopTime() - stopWatch.getStartTime(), time);
    }

    @Test
    public void testGetTimeStopWatchRunning() {
        stopWatch.start();
        long time = stopWatch.getTime();
        long expectedTime = stopWatch.getStartTime() - System.currentTimeMillis();
        assertEquals(expectedTime, time);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTimeIllegalState() {
        stopWatch.getTime();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testGetSplitTimeWithSplit() {
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        long expectedTime = stopWatch.getStopTime() - stopWatch.getStartTime();
        assertEquals(expectedTime, splitTime);
    }

    @Test
    public void testToString() {
        stopWatch.start();
        stopWatch.stop();
        String timeString = stopWatch.toString();
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getTime()), timeString);
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()), splitTimeString);
    }
}