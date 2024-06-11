package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Test class for StopWatch
 */
public class StopWatchTest {

    // Basic tests for all methods

    @org.junit.Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // Basic functionality check
        assertTrue(stopWatch.isRunning());
    }

    @org.junit.Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertEquals(2, stopWatch.getRunningStates().length);
        assertTrue(stopWatch.isSplit());
    }

    @org.junit.Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(1, stopWatch.getRunningStates().length);
        assertFalse(stopWatch.isSplit());
    }

    @org.junit.Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(1, stopWatch.getRunningStates().length);
        assertFalse(stopWatch.isSuspended());
    }

    @org.junit.Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertEquals(1, stopWatch.getRunningStates().length);
        assertTrue(stopWatch.isStopped());
        assertEquals("0:0:0.0", stopWatch.toString());
        assertEquals(time, stopWatch.getTime());
    }

    @org.junit.Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
        assertEquals(0, stopWatch.getSplitTime());
        assertEquals(1, stopWatch.getRunningStates().length);
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());
    }

    @org.junit.Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        assertEquals(1, stopWatch.getRunningStates().length);
        assertTrue(stopWatch.isRunning());
        assertEquals(time, stopWatch.getTime());
    }

    @org.junit.Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertEquals(2, stopWatch.getRunningStates().length);
        assertTrue(stopWatch.isSplit());
        assertEquals(splitTime, stopWatch.getSplitTime());
    }

    @org.junit.Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String timeString = stopWatch.toString();
        assertEquals(1, stopWatch.getRunningStates().length);
        assertTrue(stopWatch.isRunning());
        assertEquals("0:0:0.0", timeString);
    }

    @org.junit.Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        assertEquals(2, stopWatch.getRunningStates().length);
        assertTrue(stopWatch.isSplit());
        assertEquals("0:0:0.0", splitTimeString);
    }

    // Edge cases and exception handling tests

    @org.junit.Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.unsplit();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testResetWithRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    // Test methods that check for correct order of calls

    @org.junit.Test
    public void testStartSplitStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.start();
        assertEquals(3, stopWatch.getRunningStates().length);
    }

    @org.junit.Test
    public void testStartSplitStopSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.split();
        assertEquals(4, stopWatch.getRunningStates().length);
    }

    @org.junit.Test
    public void testStartSplitStopSplitStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.split();
        stopWatch.stop();
        assertEquals(5, stopWatch.getRunningStates().length);
    }

    // Test methods that check for negative time

    @org.junit.Test
    public void testRollOverNegativeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long oldTime = stopWatch.getTime();
        stopWatch.setTime(-1);
        long newTime = stopWatch.getTime();
        assertEquals(-1, oldTime);
        assertEquals(oldTime + 2147483647L, newTime);
    }

    @org.junit.Test
    public void testRollOverNegativeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long oldTime = stopWatch.getTime();
        long oldSplitTime = stopWatch.getSplitTime();
        stopWatch.setTime(-1);
        long newTime = stopWatch.getTime();
        long newSplitTime = stopWatch.getSplitTime();
        assertEquals(-1, oldTime);
        assertEquals(oldTime + 2147483647L, newTime);
        assertEquals(-1 * oldSplitTime, newSplitTime);
    }

    // Test methods that check for long running times

    @org.junit.Test
    public void testLongRunningTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long maxLong = Long.MAX_VALUE;
        while(stopWatch.getTime() < maxLong) {
            // Do nothing.
        }
        assertTrue(stopWatch.isRunning());
        assertEquals(1, stopWatch.getRunningStates().length);
        assertEquals(maxLong, stopWatch.getTime());
    }

    // Test methods that check for invalid input

    @org.junit.Test(expected = NumberFormatException.class)
    public void testInvalidInputForDurationFormatUtils() {
        DurationFormatUtils.formatDurationHMS(-1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNegativeInputForDurationFormatUtils() {
        DurationFormatUtils.formatDurationHMS(-1);
    }

    // Test methods that check for null input

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullInputForDurationFormatUtils() {
        DurationFormatUtils.formatDurationHMS(null);
    }

    // Test methods that check for empty input

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() {
        DurationFormatUtils.formatDurationHMS("");
    }

    // Test methods that check for invalid format

    @org.junit.Test(expected = ParseException.class)
    public void testInvalidFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        formatter.parse("Invalid format");
    }
}