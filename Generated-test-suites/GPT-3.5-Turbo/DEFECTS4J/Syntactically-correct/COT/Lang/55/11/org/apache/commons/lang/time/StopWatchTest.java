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
        assertEquals(-1, stopWatch.stopTime);
        assertTrue(stopWatch.startTime > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        long startTime = stopWatch.startTime;
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.runningState);
        assertTrue(stopWatch.stopTime > 0);
        assertTrue(stopWatch.startTime == startTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testResetAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
    }

    @Test(expected = IllegalStateException.class)
    public void testResetAfterSplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.reset();
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.runningState);
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
        assertEquals(-1, stopWatch.startTime);
        assertEquals(-1, stopWatch.stopTime);
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.splitState);
        assertTrue(stopWatch.stopTime > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
        assertEquals(-1, stopWatch.stopTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.runningState);
        assertTrue(stopWatch.stopTime > 0);
        assertTrue(stopWatch.startTime > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendTwice() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
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
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
        assertTrue(stopWatch.startTime > 0);
        assertTrue(stopWatch.stopTime == -1);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
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
        long time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    @Test
    public void testGetTimeBeforeStart() {
        long time = stopWatch.getTime();
        assertEquals(0, time);
    }

    @Test
    public void testGetTimeAfterStop() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = stopWatch.getSplitTime();
        assertTrue(time > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeBeforeSplit() {
        stopWatch.getSplitTime();
    }
}