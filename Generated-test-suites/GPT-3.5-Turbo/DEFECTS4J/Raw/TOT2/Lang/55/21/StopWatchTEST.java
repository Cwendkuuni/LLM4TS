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
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.runningState);
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.runningState);
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.splitState);
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.runningState);
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        // Assuming some time has elapsed for the test
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        // Assuming some time has elapsed after split for the test
        assertTrue(stopWatch.getSplitTime() >= 0);
    }

    @Test
    public void testToString() {
        stopWatch.start();
        // Assuming some time has elapsed for the test
        assertNotNull(stopWatch.toString());
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        // Assuming some time has elapsed after split for the test
        assertNotNull(stopWatch.toSplitString());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotRunning() {
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        stopWatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        stopWatch.getSplitTime();
    }

}