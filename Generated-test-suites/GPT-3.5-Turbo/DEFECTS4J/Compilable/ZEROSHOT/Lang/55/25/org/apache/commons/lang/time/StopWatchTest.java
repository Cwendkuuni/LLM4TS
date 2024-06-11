package org.apache.commons.lang.time;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assert stopWatch.getTime() == 0;
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
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
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assert stopWatch.getTime() > 0;
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assert stopWatch.getTime() == 0;
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assert stopWatch.getTime() > stopWatch.getSplitTime();
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
        assert stopWatch.getTime() == stopWatch.getSplitTime();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspendState() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.resume();
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        long time1 = stopWatch.getTime();
        stopWatch.resume();
        stopWatch.suspend();
        long time2 = stopWatch.getTime();
        assert time2 > time1;
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {Thread.sleep(1000);} catch (InterruptedException e) {}
        stopWatch.stop();
        long time = stopWatch.getTime();
        assert time >= 1000;
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getSplitTime();
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        try {Thread.sleep(1000);} catch (InterruptedException e) {}
        stopWatch.stop();
        long splitTime = stopWatch.getSplitTime();
        assert splitTime >= 1000;
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        assert stopWatch.toString().equals("0:00:00.000");
        stopWatch.start();
        try {Thread.sleep(1000);} catch (InterruptedException e) {}
        stopWatch.stop();
        assert stopWatch.toString().matches("\\d+:\\d{2}:\\d{2}.\\d{3}");
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        assert stopWatch.toSplitString().equals("0:00:00.000");
        stopWatch.start();
        stopWatch.split();
        try {Thread.sleep(1000);} catch (InterruptedException e) {}
        stopWatch.stop();
        assert stopWatch.toSplitString().matches("\\d+:\\d{2}:\\d{2}.\\d{3}");
    }
}