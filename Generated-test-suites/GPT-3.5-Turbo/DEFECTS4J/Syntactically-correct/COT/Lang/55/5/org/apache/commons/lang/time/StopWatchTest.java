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
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(-1, stopWatch.getStopTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopNotRunning() {
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(-1, stopWatch.getStopTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitNotRunning() {
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitNotSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(-1, stopWatch.getStopTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendNotRunning() {
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(-1, stopWatch.getStopTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeNotSuspended() {
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        long time = stopWatch.getTime();
        assertTrue(time >= 0);
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime >= 0);
    }

    @Test
    public void testToString() {
        stopWatch.start();
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        assertNotNull(splitTimeString);
    }
}