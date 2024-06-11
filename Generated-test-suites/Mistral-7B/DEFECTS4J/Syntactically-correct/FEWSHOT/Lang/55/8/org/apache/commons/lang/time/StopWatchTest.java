package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.ParseException;

public class StopWatchTest {

    @Test
    public void testConstructor() {
        StopWatch stopWatch = new StopWatch();
        assertNull(stopWatch.getTime());
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test
    public void testSplitRunning() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        long splitTime = stopWatch.getSplitTime();
        assertEquals(100, splitTime);
    }

    @Test
    public void testSplitSuspended() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.suspend();
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        long splitTime = stopWatch.getSplitTime();
        assertEquals(100, splitTime);
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testResumeSuspended() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        Thread.sleep(100);
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        stopWatch.stop();
        String timeString = stopWatch.toString();
        long time = Long.parseLong(timeString.substring(0, timeString.length() - 3));
        assertEquals(1000, time);
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        stopWatch.split();
        Thread.sleep(500);
        stopWatch.stop();
        String splitTimeString = stopWatch.toSplitString();
        long splitTime = Long.parseLong(splitTimeString.substring(0, splitTimeString.length() - 3));
        assertEquals(1500, splitTime);
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertEquals(1000, time);
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        stopWatch.split();
        Thread.sleep(500);
        stopWatch.stop();
        long splitTime = stopWatch.getSplitTime();
        assertEquals(1500, splitTime);
    }

    @Test
    public void testGetRunningState() {
        StopWatch stopWatch = new StopWatch();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        stopWatch.split();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    @Test
    public void testGetSplitState() {
        StopWatch stopWatch = new StopWatch();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        stopWatch.reset();
        assertFalse(stopWatch.isRunning());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testParseDurationHMS() throws ParseException {
        String time = "00:01:15.012";
        long duration = DurationFormatUtils.parseDuration(time);
        assertEquals(1151L, duration);

        time = "01:15:01.123";
        duration = DurationFormatUtils.parseDuration(time);
        assertEquals(3961131L, duration);

        time = "2:15:01.123";
        duration = DurationFormatUtils.parseDuration(time);
        assertEquals(7921131L, duration);

        time = "01:01:01.123";
        duration = DurationFormatUtils.parseDuration(time);
        assertEquals(3661131L, duration);
    }

    @Test
    public void testFormatDurationHMS() {
        long duration = 1000;
        String timeString = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals("00:01:00.010", timeString);

        duration = 1151;
        timeString = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals("00:01:15.012", timeString);

        duration = 3961131;
        timeString = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals("01:15:01.123", timeString);

        duration = 7921131;
        timeString = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals("02:15:01.123", timeString);
    }

}