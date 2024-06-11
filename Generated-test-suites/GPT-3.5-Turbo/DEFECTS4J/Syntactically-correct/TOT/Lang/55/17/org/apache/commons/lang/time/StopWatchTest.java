import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @After
    public void tearDown() {
        stopWatch = null;
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertTrue(stopWatch.getStartTime() <= System.currentTimeMillis());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopWatch.resume();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertTrue(stopWatch.getStartTime() <= stopWatch.getStopTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTimeWithUnstartedWatch() {
        stopWatch.getTime();
    }

    @Test
    public void testGetTimeWithStoppedWatch() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 0);
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
        stopWatch.unsplit();
        assertEquals(splitTime, stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        stopWatch.suspend();
    }

    @Test
    public void testSuspendAndResume() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        long suspendedTime = stopWatch.getStopTime() - stopWatch.getStartTime();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(suspendedTime, stopWatch.getStopTime() - stopWatch.getStartTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspended() {
        stopWatch.start();
        stopWatch.resume();
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
    public void testToString() {
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
        assertEquals(8, timeString.length());
        assertTrue(timeString.matches("\\d{2}:\\d{2}:\\d{2}"));
    }
    
    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        assertNotNull(splitTimeString);
        assertEquals(8, splitTimeString.length());
        assertTrue(splitTimeString.matches("\\d{2}:\\d{2}:\\d{2}"));
    }
}