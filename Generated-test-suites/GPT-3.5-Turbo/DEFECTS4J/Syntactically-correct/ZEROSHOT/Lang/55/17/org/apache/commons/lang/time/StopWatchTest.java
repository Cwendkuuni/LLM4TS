import static org.junit.Assert.*;
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
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(System.currentTimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhileRunning() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(System.currentTimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
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
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        assertEquals(System.currentTimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitNotRunning() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(System.currentTimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitNotSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(System.currentTimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
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
        assertEquals(System.currentTimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeNotSuspended() {
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test
    public void testGetTimeStopped() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= stopWatch.getStartTime());
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= stopWatch.getStartTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeNotSplit() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() {
        stopWatch.start();
        assertTrue(stopWatch.toString().startsWith("00:00:"));
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.toSplitString().startsWith("00:00:"));
    }

}