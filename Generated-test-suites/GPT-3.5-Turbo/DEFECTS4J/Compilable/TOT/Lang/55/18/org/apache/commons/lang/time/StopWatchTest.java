package org.apache.commons.lang.time;
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
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
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
    public void testStopWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        assertTrue(stopWatch.getTime() == 0);
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.getSplitTime() == 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test
    public void testGetTimeWithStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 0);
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
        stopWatch.start();
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
        assertTrue(timeString.length() > 0);
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        assertNotNull(splitTimeString);
        assertTrue(splitTimeString.length() > 0);
    }

}