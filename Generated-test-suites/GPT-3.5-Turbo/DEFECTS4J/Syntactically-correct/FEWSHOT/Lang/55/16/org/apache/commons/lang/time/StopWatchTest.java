import static org.junit.Assert.assertEquals;
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
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenRunning() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenStopped() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertNotEquals(-1, stopWatch.getStopTime());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotRunningOrSuspended() {
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertNotEquals(-1, stopWatch.getStopTime());
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWhenNotRunning() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertNotEquals(-1, stopWatch.getStopTime());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        assertNotEquals(-1, stopWatch.getStopTime());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWhenNotRunning() {
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertNotEquals(-1, stopWatch.getStopTime());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        stopWatch.resume();
    }

    @Test
    public void testGetTimeWhenStopped() {
        stopWatch.start();
        stopWatch.stop();
        assertNotEquals(0, stopWatch.getTime());
    }

    @Test
    public void testGetTimeWhenSuspended() {
        stopWatch.start();
        stopWatch.suspend();
        assertNotEquals(0, stopWatch.getTime());
    }

    @Test
    public void testGetTimeWhenUnstarted() {
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testToString() {
        stopWatch.start();
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
        assertNotEquals("", timeString);
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        assertNotNull(splitTimeString);
        assertNotEquals("", splitTimeString);
    }
}