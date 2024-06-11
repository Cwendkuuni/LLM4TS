package org.apache.commons.lang.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        assertEquals(time, stopWatch.getStopTime() - stopWatch.getStartTime());
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertEquals(splitTime, stopWatch.getStopTime() - stopWatch.getStartTime());
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String timeString = stopWatch.toString();
        assertEquals(timeString, DurationFormatUtils.formatDurationHMS(stopWatch.getTime()));
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        assertEquals(splitTimeString, DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()));
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenStopped() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotRunningOrSuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWhenNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWhenNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.resume();
    }

    private static void assertEquals(int expected, int actual) {
        org.junit.Assert.assertEquals(expected, actual);
    }

    private static void assertEquals(long expected, long actual) {
        org.junit.Assert.assertEquals(expected, actual);
    }

    private static void assertEquals(String expected, String actual) {
        org.junit.Assert.assertEquals(expected, actual);
    }

    private static void assertTrue(boolean condition) {
        org.junit.Assert.assertTrue(condition);
    }

    private static void assertFalse(boolean condition) {
        org.junit.Assert.assertFalse(condition);
    }

}