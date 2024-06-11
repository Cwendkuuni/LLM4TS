package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testNewInstance() {
        StopWatch stopWatch = new StopWatch();
        assertNull(stopWatch.getTime());
        assertSame(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    @Test
    public void testStartStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertSame(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test
    public void testSplitUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertSame(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertNull(stopWatch.getSplitTime());
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
    public void testSuspendResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertSame(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        long timeBeforeSuspend = System.currentTimeMillis(); - stopWatch.getStartTime();
        stopWatch.stop();
        long timeAfterResume = System.currentTimeMillis() - stopWatch.getStartTime();
        assertEquals(timeBeforeSuspend + timeAfterResume, stopWatch.getTime());
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
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.yield();
        Thread.yield();
        stopWatch.stop();
        assertEquals("0.001", stopWatch.toString());
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.yield();
        Thread.yield();
        stopWatch.split();
        Thread.yield();
        Thread.yield();
        stopWatch.stop();
        long expectedMilliseconds = 4;
        long expectedHours = 0;
        long expectedMinutes = 0;
        long expectedSeconds = expectedMilliseconds / 1000;
        long splitTime = stopWatch.getSplitTime();
        String formattedSplitTime = DurationFormatUtils.formatDurationHMS(splitTime);
        assertEquals(expectedHours + ":" + expectedMinutes + ":" + expectedSeconds + "." + (expectedMilliseconds % 1000), formattedSplitTime);
    }

}