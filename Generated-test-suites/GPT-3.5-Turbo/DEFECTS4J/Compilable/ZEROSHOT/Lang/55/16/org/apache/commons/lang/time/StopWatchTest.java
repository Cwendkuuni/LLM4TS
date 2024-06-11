package org.apache.commons.lang.time;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        assert stopwatch.getTime() == 0;
        assert stopwatch.getSplitTime() == 0;
    }

    @Test
    public void testStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        assert stopwatch.getTime() >= 0;
        assert stopwatch.getSplitTime() >= 0;
    }

    @Test
    public void testReset() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        stopwatch.reset();
        assert stopwatch.getTime() == 0;
        assert stopwatch.getSplitTime() == 0;
    }

    @Test
    public void testSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        long splitTime = stopwatch.getSplitTime();
        stopwatch.unsplit();
        assert stopwatch.getSplitTime() == 0;
        stopwatch.split();
        assert stopwatch.getSplitTime() >= splitTime;
    }

    @Test
    public void testSuspend() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        long timeBeforeSuspend = stopwatch.getTime();
        stopwatch.resume();
        long timeAfterResume = stopwatch.getTime();
        assert timeAfterResume >= timeBeforeSuspend;
    }

    @Test
    public void testTime() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopwatch.stop();
        assert stopwatch.getTime() >= 100;
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.getSplitTime();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhileRunning() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.start();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStopWhileStopped() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.stop();
        stopwatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testResetWhileRunning() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.reset();
    }

}