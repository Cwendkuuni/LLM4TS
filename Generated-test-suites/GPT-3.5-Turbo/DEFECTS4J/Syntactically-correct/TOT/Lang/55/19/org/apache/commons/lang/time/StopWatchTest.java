import org.junit.Before;
import org.junit.Test;
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
        assertTrue(stopwatch.getTime() >= 0);
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.getState());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        stopwatch.start();
        stopwatch.start();
    }

    @Test
    public void testStop() {
        stopwatch.start();
        stopwatch.stop();
        assertTrue(stopwatch.getTime() >= 0);
        assertEquals(StopWatch.STATE_STOPPED, stopwatch.getState());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        stopwatch.stop();
    }

    @Test
    public void testReset() {
        stopwatch.start();
        stopwatch.reset();
        assertTrue(stopwatch.getTime() == 0);
        assertEquals(StopWatch.STATE_UNSTARTED, stopwatch.getState());
    }

    @Test
    public void testSplit() {
        stopwatch.start();
        stopwatch.split();
        assertTrue(stopwatch.getSplitTime() >= 0);
        assertEquals(StopWatch.STATE_SPLIT, stopwatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        stopwatch.split();
    }

    @Test
    public void testUnsplit() {
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopwatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopwatch.start();
        stopwatch.suspend();
        assertTrue(stopwatch.getTime() >= 0);
        assertEquals(StopWatch.STATE_SUSPENDED, stopwatch.getState());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        stopwatch.suspend();
    }

    @Test
    public void testResume() {
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.getState());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopwatch.resume();
    }

    @Test
    public void testGetTime() {
        stopwatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopwatch.stop();
        long time = stopwatch.getTime();
        assertTrue(time >= 1000 && time < 1100);
    }

    @Test(expected = RuntimeException.class)
    public void testIllegalRunningState() {
        stopwatch.resume();
    }

    @Test
    public void testToString() {
        stopwatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopwatch.stop();
        String timeString = stopwatch.toString();
        assertNotNull(timeString);
        assertEquals(11, timeString.length());
        assertTrue(timeString.matches("^\\d{2}:\\d{2}:\\d{2}$"));
    }

    @Test
    public void testToSplitString() {
        stopwatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopwatch.split();
        String splitTimeString = stopwatch.toSplitString();
        assertNotNull(splitTimeString);
        assertEquals(11, splitTimeString.length());
        assertTrue(splitTimeString.matches("^\\d{2}:\\d{2}:\\d{2}$"));
    }
}