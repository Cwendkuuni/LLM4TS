import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAlreadyStarted() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAfterStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAfterSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAfterUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(stopWatch.getTime(), stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeAfterStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.resume();
    }

    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTimeUnstarted() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getTime();
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        assertNotNull(splitTimeString);
    }
}