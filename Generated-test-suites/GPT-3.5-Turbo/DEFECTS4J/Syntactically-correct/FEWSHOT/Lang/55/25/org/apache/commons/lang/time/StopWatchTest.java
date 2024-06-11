import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StopWatchTest {
    @Test
    public void testStart() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertEquals(0, watch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        StopWatch watch = new StopWatch();
        watch.stop();
    }

    @Test
    public void testStop() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertEquals(0, watch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.stop();
    }

    @Test
    public void testReset() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.reset();
        assertEquals(0, watch.getTime());
    }

    @Test
    public void testSplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertEquals(watch.getTime(), watch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch watch = new StopWatch();
        watch.unsplit();
    }

    @Test
    public void testUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.unsplit();
        assertEquals(0, watch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        StopWatch watch = new StopWatch();
        watch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.suspend();
    }

    @Test
    public void testSuspend() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(10);
        watch.suspend();
        assertEquals(10, watch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch watch = new StopWatch();
        watch.resume();
    }

    @Test
    public void testResume() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(10);
        watch.suspend();
        Thread.sleep(10);
        watch.resume();
        assertEquals(20, watch.getTime());
    }

    @Test
    public void testGetTimeWhileStopped() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertEquals(0, watch.getTime());
    }

    @Test
    public void testGetTimeWhileSuspended() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        assertEquals(0, watch.getTime());
    }

    @Test(expected = RuntimeException.class)
    public void testGetTimeWithIllegalState() {
        StopWatch watch = new StopWatch();
        watch.getTime();
    }

    @Test(expected = IllegalStateException.class)
    public void testgetSplitTimeWithoutSplit() {
        StopWatch watch = new StopWatch();
        watch.getSplitTime();
    }

    @Test
    public void testGetSplitTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertEquals(watch.getTime(), watch.getSplitTime());
    }

    @Test
    public void testToString() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertEquals("0", watch.toString());
    }

    @Test
    public void testToSplitString() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertEquals("0", watch.toSplitString());
    }
}