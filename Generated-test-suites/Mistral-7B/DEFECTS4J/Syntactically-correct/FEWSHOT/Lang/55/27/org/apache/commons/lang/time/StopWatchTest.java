package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.ParseException;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testConstructors() {
        new StopWatch();
    }

    @Test
    public void testGetters() {
        StopWatch stopWatch = new StopWatch();

        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStarted());
    }

    @Test
    public void testStartStop() {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        stopWatch.stop();

        assertEquals(0L, stopWatch.getTime());
        assertFalse(stopWatch.isRunning());
        assertNull(stopWatch.getSplitTime());
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
    public void testReset() {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        stopWatch.stop();

        stopWatch.reset();

        assertEquals(0L, stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        long splitTime = stopWatch.split();

        assertEquals(0L, stopWatch.getTime());
        assertEquals(splitTime, stopWatch.getSplitTime());
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStarted());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        stopWatch.split();

        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        long splitTime = stopWatch.split();

        stopWatch.unsplit();

        assertEquals(0L, stopWatch.getSplitTime());
        assertEquals(splitTime, stopWatch.getTime());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStarted());
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

        assertEquals(0L, stopWatch.getTime());
        assertFalse(stopWatch.isRunning());
        assertNull(stopWatch.getSplitTime());
        assertFalse(stopWatch.isSplit());
        assertTrue(stopWatch.isSuspended());
        assertFalse(stopWatch.isStarted());
    }

    @Test
    public void testSuspendTwice() {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();

        assertEquals(0L, stopWatch.getTime());
        assertFalse(stopWatch.isRunning());
        assertNull(stopWatch.getSplitTime());
        assertTrue(stopWatch.isSuspended());
        assertFalse(stopWatch.isStarted());
    }

    @Test
    public void testResumeBeforeSuspend() {
        StopWatch stopWatch = new StopWatch();

        stopWatch.resume();
    }

    @Test
    public void testToString() throws ParseException {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Thread.sleep(2000);
        stopWatch.stop();

        String timeString = stopWatch.toString();
        String expectedTimeString = DurationFormatUtils.formatDurationHMS(stopWatch.getTime());

        assertEquals(expectedTimeString, timeString);
    }

    @Test
    public void testToSplitString() throws ParseException {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Thread.sleep(2000);
        stopWatch.split();
        Thread.sleep(3000);
        stopWatch.unsplit();

        String timeString = stopWatch.toSplitString();
        String expectedTimeString = DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime());

        assertEquals(expectedTimeString, timeString);
    }
}