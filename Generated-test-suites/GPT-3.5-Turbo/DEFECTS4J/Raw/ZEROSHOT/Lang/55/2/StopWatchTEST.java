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
    }

    @Test(expected = IllegalStateException.class)
    public void testStart_ThrowsIllegalStateException() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStart_ThrowsIllegalStateExceptionWhenRestartingAfterReset() {
        stopWatch.start();
        stopWatch.reset();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testStop_ThrowsIllegalStateExceptionWhenAlreadyStopped() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testStop_ThrowsIllegalStateExceptionWhenNotRunning() {
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
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplit_ThrowsIllegalStateExceptionWhenNotRunning() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplit_ThrowsIllegalStateExceptionWhenNotSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspend_ThrowsIllegalStateExceptionWhenNotRunning() {
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testResume_ThrowsIllegalStateExceptionWhenNotSuspended() {
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertTrue(time >= 1000 && time <= 1100);
    }

    @Test
    public void testGetTime_WhenRunning() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = stopWatch.getTime();
        assertTrue(time >= 1000 && time <= 1100);
    }

    @Test
    public void testGetTime_WhenStopped() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertTrue(time >= 1000 && time <= 1100);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTime_ThrowsRuntimeExceptionWhenIllegalRunningState() {
        stopWatch.getTime();
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.unsplit();
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime >= 1000 && splitTime <= 1100);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTime_ThrowsIllegalStateExceptionWhenNotSplit() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        String timeString = stopWatch.toString();
        assertTrue(timeString.matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.unsplit();
        String splitTimeString = stopWatch.toSplitString();
        assertTrue(splitTimeString.matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
    }
}