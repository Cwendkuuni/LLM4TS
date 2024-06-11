import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {
    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
        assertEquals(-1, stopWatch.startTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenRunning() {
        stopWatch.runningState = StopWatch.STATE_RUNNING;
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenStopped() {
        stopWatch.runningState = StopWatch.STATE_STOPPED;
        stopWatch.start();
    }

    @Test
    public void testStop() {
        stopWatch.runningState = StopWatch.STATE_RUNNING;
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.runningState);
        assertNotEquals(-1, stopWatch.stopTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotRunningOrSuspended() {
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        stopWatch.runningState = StopWatch.STATE_RUNNING;
        stopWatch.splitState = StopWatch.STATE_SPLIT;
        stopWatch.startTime = 1000;
        stopWatch.stopTime = 2000;
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.runningState);
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
        assertEquals(-1, stopWatch.startTime);
        assertEquals(-1, stopWatch.stopTime);
    }

    @Test
    public void testSplit() {
        stopWatch.runningState = StopWatch.STATE_RUNNING;
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.splitState);
        assertNotEquals(-1, stopWatch.stopTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWhenNotRunning() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.splitState = StopWatch.STATE_SPLIT;
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
        assertEquals(-1, stopWatch.stopTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.runningState = StopWatch.STATE_RUNNING;
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.runningState);
        assertNotEquals(-1, stopWatch.stopTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWhenNotRunning() {
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        stopWatch.runningState = StopWatch.STATE_SUSPENDED;
        stopWatch.startTime = 1000;
        stopWatch.stopTime = 2000;
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
        assertEquals(1000, stopWatch.startTime);
        assertEquals(-1, stopWatch.stopTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        stopWatch.resume();
    }

    @Test
    public void testGetTimeWhenStoppedOrSuspended() {
        stopWatch.runningState = StopWatch.STATE_STOPPED;
        stopWatch.startTime = 1000;
        stopWatch.stopTime = 2000;
        long time = stopWatch.getTime();
        assertEquals(1000, time);
    }

    @Test
    public void testGetTimeWhenUnstarted() {
        long time = stopWatch.getTime();
        assertEquals(0, time);
    }

    @Test
    public void testGetTimeWhenRunning() {
        stopWatch.runningState = StopWatch.STATE_RUNNING;
        stopWatch.startTime = 1000;
        long currentTime = System.currentTimeMillis();
        long time = stopWatch.getTime();
        assertTrue(time >= 0 && time <= (currentTime - 1000));
    }

    @Test(expected = RuntimeException.class)
    public void testGetTimeWhenIllegalRunningState() {
        stopWatch.runningState = 4;
        stopWatch.getTime();
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.splitState = StopWatch.STATE_SPLIT;
        stopWatch.startTime = 1000;
        stopWatch.stopTime = 2000;
        long time = stopWatch.getSplitTime();
        assertEquals(1000, time);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhenNotSplit() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() {
        stopWatch.startTime = 1000;
        stopWatch.stopTime = 2000;
        String timeString = stopWatch.toString();
        assertEquals("00:00:01.000", timeString);
    }

    @Test
    public void testToSplitString() {
        stopWatch.splitState = StopWatch.STATE_SPLIT;
        stopWatch.startTime = 1000;
        stopWatch.stopTime = 2000;
        String timeString = stopWatch.toSplitString();
        assertEquals("00:00:01.000", timeString);
    }
}