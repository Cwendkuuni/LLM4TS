package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;
import java.text.ParsePosition;

/**
 * Test cases for StopWatch class
 */
public class StopWatchTest {

    @org.junit.Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    private StopWatch stopWatch;

    /**
     * Test for constructor
     */
    @org.junit.Test
    public void testConstructor() {
        assertNull(stopWatch.getTime());
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    /**
     * Test for start() method
     */
    @org.junit.Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    /**
     * Test for start() method when already running
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testStartWhileRunning() {
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test for split() method
     */
    @org.junit.Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isSplit());
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    /**
     * Test for split() method when not running
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSplitWhileNotRunning() {
        stopWatch.split();
    }

    /**
     * Test for unsplit() method
     */
    @org.junit.Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse(stopWatch.isSplit());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    /**
     * Test for unsplit() method when not split
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testUnsplitWhileNotSplit() {
        stopWatch.unsplit();
    }

    /**
     * Test for suspend() method
     */
    @org.junit.Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertFalse(stopWatch.isRunning());
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    /**
     * Test for suspend() method when not running
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSuspendWhileNotRunning() {
        stopWatch.suspend();
    }

    /**
     * Test for resume() method
     */
    @org.junit.Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    /**
     * Test for resume() method when not suspended
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testResumeWhileNotSuspended() {
        stopWatch.resume();
    }

    /**
     * Test for getTime() method
     */
    @org.junit.Test
    public void testGetTime() {
        long startTime = System.currentTimeMillis();
        stopWatch.start();
        long elapsed = stopWatch.getTime();
        long expectedElapsed = System.currentTimeMillis() - startTime;
        assertEquals(expectedElapsed, elapsed);
    }

    /**
     * Test for getTime() method when not running
     */
    @org.junit.Test
    public void testGetTimeWhileNotRunning() {
        long expectedElapsed = 0;
        long actualElapsed = stopWatch.getTime();
        assertEquals(expectedElapsed, actualElapsed);
    }

    /**
     * Test for getSplitTime() method
     */
    @org.junit.Test
    public void testGetSplitTime() throws ParseException {
        stopWatch.start();
        stopWatch.split();
        long elapsed = stopWatch.getSplitTime();
        long expectedElapsed = System.currentTimeMillis() - stopWatch.getStartTime();
        ParsePosition position = new ParsePosition(0);
        long milliseconds = Long.parseLong(DurationFormatUtils.formatDurationHMS(expectedElapsed).substring(0,
                DurationFormatUtils.formatDurationHMS(expectedElapsed).length() - 3).split(":")[2]);
        long seconds = Long.parseLong(DurationFormatUtils.formatDurationHMS(expectedElapsed).substring(0,
                DurationFormatUtils.formatDurationHMS(expectedElapsed).length() - 6).split(":")[1]);
        long minutes = Long.parseLong(DurationFormatUtils.formatDurationHMS(expectedElapsed).substring(0,
                DurationFormatUtils.formatDurationHMS(expectedElapsed).length() - 9).split(":")[0]);
        long hours = 0;
        if (minutes > 60) {
            hours = minutes / 60;
            minutes = minutes % 60;
        }
        long actualMilliseconds = Long.parseLong(DurationFormatUtils.parseDuration(DurationFormatUtils.formatDuration(elapsed), position).toString().split(" ")[0]);
        assertEquals(hours * 60 * 60 * 1000 + minutes * 60 * 1000 + milliseconds, actualMilliseconds);
    }

    /**
     * Test for getSplitTime() method when not split
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhileNotSplit() {
        stopWatch.getSplitTime();
    }

    /**
     * Test for toString() method
     */
    @org.junit.Test
    public void testToString() {
        stopWatch.start();
        String expectedToString = "00:00:01.000";
        String actualToString = stopWatch.toString();
        assertEquals(expectedToString, actualToString);
    }

    /**
     * Test for toSplitString() method
     */
    @org.junit.Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String expectedToString = "00:00:01.000";
        String actualToString = stopWatch.toSplitString();
        assertEquals(expectedToString, actualToString);
    }
}