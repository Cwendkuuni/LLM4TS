package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;

/**
 * Test class for StopWatch class.
 */
public class StopWatchTest {

    @org.junit.Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    private StopWatch stopWatch;

    /**
     * Test for start() method.
     */
    @org.junit.Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertEquals(STATE_RUNNING, stopWatch.getRunningState());
    }

    /**
     * Test for stop() method.
     */
    @org.junit.Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(STATE_STOPPED, stopWatch.getRunningState());
        assertTrue(stopWatch.isStopped());
    }

    /**
     * Test for split() method.
     */
    @org.junit.Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(STATE_SPLIT, stopWatch.getSplitState());
        assertTrue(stopWatch.isSplit());
    }

    /**
     * Test for unsplit() method.
     */
    @org.junit.Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(STATE_UNSPLIT, stopWatch.getSplitState());
        assertFalse(stopWatch.isSplit());
    }

    /**
     * Test for suspend() method.
     */
    @org.junit.Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(STATE_RUNNING, stopWatch.getRunningState());
    }

    /**
     * Test for getTime() method.
     */
    @org.junit.Test
    public void testGetTime() {
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    /**
     * Test for getSplitTime() method.
     */
    @org.junit.Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime > 0);
    }

    /**
     * Test for toString() method.
     */
    @org.junit.Test
    public void testToString() {
        stopWatch.start();
        stopWatch.stop();
        String toString = stopWatch.toString();
        String expectedString = "0s";
        assertEquals(expectedString, toString);
    }

    /**
     * Test for toSplitString() method.
     */
    @org.junit.Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String toSplitString = stopWatch.toSplitString();
        String expectedString = "0s";
        assertEquals(expectedString, toSplitString);
    }

    /**
     * Test for illegal method calls.
     */
    @org.junit.Test
    public void testIllegalMethodCalls() {
        stopWatch.start();

        // Test illegal call to stop() before start()
        try {
            stopWatch.stop();
            fail("Illegal call to stop() before start()");
        } catch (IllegalStateException e) {
            // expected
        }

        // Test illegal call to split() before start()
        try {
            stopWatch.split();
            fail("Illegal call to split() before start()");
        } catch (IllegalStateException e) {
            // expected
        }

        // Test illegal call to split() after stop()
        stopWatch.stop();
        try {
            stopWatch.split();
            fail("Illegal call to split() after stop()");
        } catch (IllegalStateException e) {
            // expected
        }

        // Test illegal call to unsplit() before split()
        try {
            stopWatch.unsplit();
            fail("Illegal call to unsplit() before split()");
        } catch (IllegalStateException e) {
            // expected
        }

        // Test illegal call to resume() before suspend()
        try {
            stopWatch.resume();
            fail("Illegal call to resume() before suspend()");
        } catch (IllegalStateException e) {
            // expected
        }

        // Test illegal call to start() after start()
        try {
            stopWatch.start();
            stopWatch.start();
            fail("Illegal call to start() after start()");
        } catch (IllegalStateException e) {
            // expected
        }

        // Test illegal call to start() after reset()
        stopWatch.reset();
        try {
            stopWatch.start();
            fail("Illegal call to start() after reset()");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    /**
     * Test for invalid running state.
     */
    @org.junit.Test
    public void testInvalidRunningState() {
        stopWatch.reset();
        stopWatch.runningState = 5;
        try {
            stopWatch.getTime();
            fail("Illegal running state has occured.");
        } catch (RuntimeException e) {
            // expected
        }
    }

    /**
     * Test for negative time.
     */
    @org.junit.Test
    public void testNegativeTime() {
        stopWatch.start();
        try {
            Thread.sleep(-1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    /**
     * Test for zero time.
     */
    @org.junit.Test
    public void testZeroTime() {
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertEquals(0, time);
    }

    /**
     * Test for parsing invalid duration string.
     */
    @org.junit.Test
    public void testParseInvalidDurationString() {
        try {
            long time = Long.parseLong(DurationFormatUtils.formatDurationHMS("-1s"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            long time = Long.parseLong(DurationFormatUtils.formatDurationHMS("1h-1s"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            long time = Long.parseLong(DurationFormatUtils.formatDurationHMS("1h1m"));
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }
}