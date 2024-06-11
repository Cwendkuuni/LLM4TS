import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StopWatchTest {
    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAlreadyStarted() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAfterReset() {
        stopWatch.reset();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopNotRunning() {
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        assertEquals(stopWatch.getSplitTime(), stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitNotRunning() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.unsplit();
        assertEquals(stopWatch.getTime(), stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitNotSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendNotRunning() {
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeNotSuspended() {
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test
    public void testToString() {
        stopWatch.start();
        stopWatch.stop();
        String timeString = stopWatch.toString();
        assertTrue(timeString.matches("^\\d{2}:\\d{2}:\\d{2}\\.\\d{3}$"));
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        assertTrue(stopWatch.getSplitTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeNotSplit() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String splitTimeString = stopWatch.toSplitString();
        assertTrue(splitTimeString.matches("^\\d{2}:\\d{2}:\\d{2}\\.\\d{3}$"));
    }
}