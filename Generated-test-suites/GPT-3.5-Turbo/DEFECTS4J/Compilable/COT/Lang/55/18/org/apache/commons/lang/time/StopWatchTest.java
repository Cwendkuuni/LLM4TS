package org.apache.commons.lang.time;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang.time.StopWatch;

public class StopWatchTest {

    @Test
    public void testStopWatch() {
        StopWatch stopWatch = new StopWatch();
        assertNotNull(stopWatch);
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() > 0);
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= 0);
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
    public void testSuspendWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        long time1 = stopWatch.getTime();
        assertTrue(time1 >= 0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.resume();
        long time2 = stopWatch.getTime();
        assertTrue(time2 > time1);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
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

    @Test(expected = RuntimeException.class)
    public void testIllegalState() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getTime();
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.toString().matches("\\d+:\\d+:\\d+.\\d+"));
    }

    @Test(expected = IllegalStateException.class)
    public void testToSplitStringWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.toSplitString();
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.toSplitString().matches("\\d+:\\d+:\\d+.\\d+"));
    }

}