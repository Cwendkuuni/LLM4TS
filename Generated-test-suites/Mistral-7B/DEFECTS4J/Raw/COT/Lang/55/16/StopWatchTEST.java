package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;
import java.text.ParsePosition;

/**
 * StopWatch test case.
 */
public class StopWatchTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        new StopWatch();
    }

    /**
     * Test start method.
     */
    @org.junit.Test
    public void testStart() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertTrue(watch.isRunning());
        watch.stop();
        assertFalse(watch.isRunning());
        assertEquals(-1, watch.getStartTime());
        assertEquals(-1, watch.getStopTime());
        assertEquals(0, watch.getTime());
        assertEquals("0:0:0.0", watch.toString());
    }

    /**
     * Test split method.
     * @throws InterruptedException
     */
    @org.junit.Test
    public void testSplit() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(50); // let it run for a bit
        watch.split();
        watch.stop();
        assertEquals(-1, watch.getStartTime());
        assertEquals(-1, watch.getStopTime());
        assertEquals(50, watch.getSplitTime());
        assertEquals("0:0:0.05", watch.toString());
        assertEquals("0:0:0.05", watch.toSplitString());
        assertEquals(50, watch.getTime());
    }

    /**
     * Test unsplit method.
     */
    @org.junit.Test
    public void testUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.unsplit();
        watch.stop();
        assertEquals(-1, watch.getStartTime());
        assertEquals(-1, watch.getStopTime());
        assertEquals(0, watch.getSplitTime());
        assertEquals("0:0:0.0", watch.toString());
        assertEquals(0, watch.getTime());
    }

    /**
     * Test suspend and resume methods.
     * @throws InterruptedException
     */
    @org.junit.Test
    public void testSuspendAndResume() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(50); // let it run for a bit
        watch.suspend();
        Thread.sleep(100);
        watch.resume();
        watch.stop();
        assertEquals(-1, watch.getStartTime());
        assertEquals(-1, watch.getStopTime());
        assertEquals(150, watch.getTime());
        assertEquals("0:0:1.5", watch.toString());
    }

    /**
     * Test getTime method.
     */
    @org.junit.Test
    public void testGetTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.stop();
        watch.reset();
        assertEquals(0, watch.getTime());
        watch.start();
        watch.split();
        watch.stop();
        assertEquals(2, watch.getTime());
    }

    /**
     * Test illegal states.
     * @throws InterruptedException
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testIllegalStates() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.stop();
        watch.start();
        watch.stop();
        watch.split();
        watch.split();
        watch.unsplit();
        watch.unsplit();
        watch.resume();
        watch.resume();
        watch.start();
        watch.start();
        watch.reset();
    }

    /**
     * Test toString method.
     */
    @org.junit.Test
    public void testToString() {
        StopWatch watch = new StopWatch();
        String result = watch.toString();
        ParsePosition pos = new ParsePosition(0);
        long time = Long.parseLong(result.substring(0, result.length() - 1));
        long expected = watch.getTime();
        assertEquals(expected, time);
    }

    /**
     * Test toSplitString method.
     */
    @org.junit.Test
    public void testToSplitString() {
        StopWatch watch = new StopWatch();
        String result = watch.toSplitString();
        ParsePosition pos = new ParsePosition(0);
        long time = Long.parseLong(result.substring(0, result.length() - 1));
        long expected = watch.getSplitTime();
        assertEquals(expected, time);
    }

    /**
     * Test IllegalArgumentException for negative time.
     * @throws ParseException
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNegativeTime() throws ParseException {
        String time = "-1:0:0.1";
        long millis = DurationFormatUtils.parseDuration(time, new ParsePosition(0));
        fail("Expected IllegalArgumentException");
    }
}