import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAlreadyRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.runningState = StopWatch.STATE_RUNNING;
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAfterStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.runningState = StopWatch.STATE_STOPPED;
        stopWatch.start();
    }

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.runningState = StopWatch.STATE_RUNNING;
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.runningState);
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
        assertEquals(-1, stopWatch.startTime);
        assertEquals(-1, stopWatch.stopTime);
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.runningState = StopWatch.STATE_RUNNING;
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.splitState);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.splitState = StopWatch.STATE_SPLIT;
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
    }

    @Test(expected = IllegalStateStateException.class)
    public void testUnsplitNotSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.runningState = StopWatch.STATE_RUNNING;
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.runningState = StopWatch.STATE_SUSPENDED;
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeNotSuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stopTime = System.currentTimeMillis();
        stopWatch.startTime = stopWatch.stopTime - 1000;
        assertEquals(1000, stopWatch.getTime());
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stopTime = System.currentTimeMillis();
        stopWatch.startTime = stopWatch.stopTime - 1000;
        stopWatch.splitState = StopWatch.STATE_SPLIT;
        assertEquals(1000, stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeNotSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stopTime = System.currentTimeMillis();
        stopWatch.startTime = stopWatch.stopTime - 1000;
        assertEquals("0:00:01.000", stopWatch.toString());
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stopTime = System.currentTimeMillis();
        stopWatch.startTime = stopWatch.stopTime - 1000;
        stopWatch.splitState = StopWatch.STATE_SPLIT;
        assertEquals("0:00:01.000", stopWatch.toSplitString());
    }

}