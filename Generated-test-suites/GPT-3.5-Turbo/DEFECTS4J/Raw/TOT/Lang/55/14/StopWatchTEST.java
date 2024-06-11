import org.junit.*;
import static org.junit.Assert.*;

public class StopWatchTest {
    private StopWatch stopwatch;

    @Before
    public void setUp() {
        stopwatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopwatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        stopwatch.start();
        stopwatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        stopwatch.stop();
    }

    @Test
    public void testStop() {
        stopwatch.start();
        stopwatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopwatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        stopwatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        stopwatch.start();
        stopwatch.split();
        stopwatch.split();
    }

    @Test
    public void testSplit() {
        stopwatch.start();
        stopwatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopwatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopwatch.unsplit();
    }

    @Test
    public void testUnsplit() {
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        stopwatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendTwice() {
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.suspend();
    }

    @Test
    public void testSuspend() {
        stopwatch.start();
        stopwatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopwatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopwatch.resume();
    }

    @Test
    public void testResume() {
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.getRunningState());
    }

    @Test
    public void testReset() {
        stopwatch.start();
        stopwatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopwatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.getSplitState());
    }

    @Test
    public void testGetTimeBeforeStop() {
        stopwatch.start();
        assertTrue(stopwatch.getTime() > 0);
    }

    @Test
    public void testGetTimeAfterStop() {
        stopwatch.start();
        stopwatch.stop();
        assertTrue(stopwatch.getTime() >= 0);
    }

    @Test
    public void testGetTimeBeforeSuspend() {
        stopwatch.start();
        stopwatch.suspend();
        assertTrue(stopwatch.getTime() > 0);
    }

    @Test
    public void testGetTimeAfterSuspend() {
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        assertTrue(stopwatch.getTime() > 0);
    }

    @Test
    public void testGetSplitTimeBeforeSplit() {
        stopwatch.start();
        assertTrue(stopwatch.getSplitTime() >= 0);
    }

    @Test
    public void testGetSplitTimeAfterSplit() {
        stopwatch.start();
        stopwatch.split();
        assertTrue(stopwatch.getSplitTime() >= 0);
    }

    @After
    public void tearDown() {
        stopwatch = null;
    }
}