package org.apache.commons.lang.time;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;

/**
 * Test class for StopWatch class
 */
public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertNotEquals(-1, time);
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
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        Assert.assertNotEquals(-1, splitTime);
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        Assert.assertEquals(splitTime + time, stopWatch.getTimeFromStart());
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

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        long pausedTime = System.currentTimeMillis() - stopWatch.getTime();
        stopWatch.resume();
        long resumedTime = stopWatch.getTime() - pausedTime;
        Assert.assertEquals(stopWatch.getTimeFromStart(), stopWatch.getTime() + resumedTime);
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
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        Assert.assertEquals(-1, stopWatch.getTime());
        Assert.assertEquals(-1, stopWatch.getSplitTime());
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String toString = stopWatch.toString();
        Assert.assertTrue(toString.matches("^[0-9]+:[0-5][0-9]:[0-5][0-9]:[0-5][0-9]:[0-9]{3}$"));

        stopWatch.unsplit();
        stopWatch.stop();
        String finalToString = stopWatch.toString();
        Assert.assertTrue(finalToString.matches("^[0-9]+:[0-5][0-9]:[0-5][0-9]:[0-5][0-9]:[0-9]{3}$"));
    }

    private long getTimeFromStart(StopWatch stopWatch) {
        if (stopWatch.getRunningState() == StopWatch.STATE_STOPPED) {
            return stopWatch.getTime();
        } else {
            long currentTime = System.currentTimeMillis();
            return currentTime - stopWatch.getStartTime();
        }
    }

    @Test
    public void testGetTimeFormat() throws ParseException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String toString = stopWatch.toString();
        String splitToString = stopWatch.toSplitString();
        long time = Long.parseLong(toString.split(":")[0]) * 3600000L
                + Long.parseLong(toString.split(":")[1]) * 60000L
                + Long.parseLong(toString.split(":")[2]) * 60000L
                + Long.parseLong(toString.split(":")[3]) * 1000L;
        long splitTime = Long.parseLong(splitToString.split(":")[0]) * 3600000L
                + Long.parseLong(splitToString.split(":")[1]) * 60000L
                + Long.parseLong(splitToString.split(":")[2]) * 60000L
                + Long.parseLong(splitToString.split(":")[3]) * 1000L;
        ParsePosition position = new ParsePosition(0);
        long finalTime = Long.parseLong(DurationFormatUtils.formatDurationHMS(time + splitTime), position);
        Assert.assertEquals(time + splitTime, finalTime);
    }
}