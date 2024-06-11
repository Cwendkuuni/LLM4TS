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

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= 0);
        stopWatch.unsplit();
        assertEquals(stopWatch.getTime(), stopWatch.getSplitTime());
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        long suspendedTime = stopWatch.getTime();
        stopWatch.resume();
        assertEquals(stopWatch.getTime(), suspendedTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

}
