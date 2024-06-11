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
        assertEquals(-1, stopWatch.getStopTime());
        assertTrue(stopWatch.getStartTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertTrue(stopWatch.getStartTime() > 0);
        assertTrue(stopWatch.getStopTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        stopWatch.start();
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
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        assertTrue(stopWatch.getStopTime() > 0);
        assertTrue(stopWatch.getStartTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStopTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        assertTrue(stopWatch.getStopTime() > 0);
        assertTrue(stopWatch.getStartTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertTrue(stopWatch.getStartTime() > 0);
        assertEquals(-1, stopWatch.getStopTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopWatch.resume();
    }

    @Test
    public void testGetTimeWhenStopped() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() > 0);
    }

    @Test
    public void testGetTimeWhenSuspended() {
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.getTime() > 0);
    }

    @Test
    public void testGetTimeWhenUnstarted() {
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testGetTimeWhenRunning() {
        stopWatch.start();
        assertTrue(stopWatch.getTime() > 0);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTimeWhenIllegalState() {
        stopWatch.getTime();
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() {
        stopWatch.start();
        String expected = stopWatch.getTime() + "ms";
        assertEquals(expected, stopWatch.toString());
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String expected = stopWatch.getSplitTime() + "ms";
        assertEquals(expected, stopWatch.toSplitString());
    }

}