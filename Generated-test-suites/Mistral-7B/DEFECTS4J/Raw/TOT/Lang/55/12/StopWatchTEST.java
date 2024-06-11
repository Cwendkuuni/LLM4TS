package org.apache.commons.lang.time;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // assuming the test runs immediately after start and stop
        assertEquals("Time should be equal to zero when start and stop are called in quick succession", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long splitTime = stopWatch.splitTime(); // assume this call does not affect the running state
        stopWatch.unsplit();
        long actualTime = stopWatch.getTime();
        long expectedTime = splitTime;
        assertEquals("Time should be equal to the split time when split and unsplit are called", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - startTime;
        stopWatch.resume();
        long actualTime = stopWatch.getTime();
        long expectedTime = startTime + (System.currentTimeMillis() - pauseTime);
        assertEquals("Time should be equal to the original start time plus the elapsed time after suspend and resume", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        stopWatch.reset();
        long expectedTime = 0;
        assertEquals("Time should be reset to zero after calling reset", expectedTime, actualTime);
    }
}