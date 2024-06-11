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
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopwatch.start();
        stopwatch.start();
    }

    @Test
    public void testStop() throws InterruptedException {
        stopwatch.start();
        Thread.sleep(1000);
        stopwatch.stop();
        assertTrue(stopwatch.getTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        stopwatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        stopwatch.start();
        stopwatch.stop();
        stopwatch.stop();
    }

    @Test
    public void testReset() {
        stopwatch.start();
        stopwatch.stop();
        stopwatch.reset();
        assertEquals(0, stopwatch.getTime());
    }

    @Test
    public void testSplit() throws InterruptedException {
        stopwatch.start();
        Thread.sleep(1000);
        stopwatch.split();
        Thread.sleep(1000);
        stopwatch.unsplit();
        assertEquals(stopwatch.getTime(), stopwatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        stopwatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopwatch.start();
        stopwatch.unsplit();
    }

    @Test
    public void testSuspend() throws InterruptedException {
        stopwatch.start();
        Thread.sleep(1000);
        stopwatch.suspend();
        Thread.sleep(1000);
        stopwatch.resume();
        assertEquals(1000, stopwatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        stopwatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopwatch.start();
        stopwatch.resume();
    }

    @Test
    public void testGetTime() throws InterruptedException {
        stopwatch.start();
        Thread.sleep(1000);
        long time1 = stopwatch.getTime();
        Thread.sleep(1000);
        long time2 = stopwatch.getTime();
        assertTrue(time2 > time1);
    }

    @Test
    public void testGetSplitTime() throws InterruptedException {
        stopwatch.start();
        Thread.sleep(1000);
        stopwatch.split();
        Thread.sleep(1000);
        long splitTime = stopwatch.getSplitTime();
        Thread.sleep(1000);
        long time = stopwatch.getTime();
        assertEquals(time, splitTime + stopwatch.getSplitTime());
    }

    @Test
    public void testToString() {
        stopwatch.start();
        stopwatch.stop();
        assertNotNull(stopwatch.toString());
    }

    @Test
    public void testToSplitString() throws InterruptedException {
        stopwatch.start();
        Thread.sleep(1000);
        stopwatch.split();
        Thread.sleep(1000);
        assertNotNull(stopwatch.toSplitString());
    }

}