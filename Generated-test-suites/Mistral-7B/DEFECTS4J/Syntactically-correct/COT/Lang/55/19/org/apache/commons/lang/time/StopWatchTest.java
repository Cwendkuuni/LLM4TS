package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Test class for StopWatch class.
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertEquals(STATE_RUNNING, stopWatch.getRunningState());
    }

    /**
     * Test start method with exception.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test stop method.
     */
    @org.junit.Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertFalse(stopWatch.isRunning());
        assertEquals(STATE_STOPPED, stopWatch.getRunningState());
    }

    /**
     * Test stop method with exception.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    /**
     * Test reset method.
     */
    @org.junit.Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        assertTrue(stopWatch.isRunning());
        assertEquals(STATE_UNSTARTED, stopWatch.getRunningState());
    }

    /**
     * Test split method.
     */
    @org.junit.Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isSplit());
        assertEquals(STATE_SPLIT, stopWatch.getSplitState());
    }

    /**
     * Test split method with exception.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    /**
     * Test unsplit method.
     */
    @org.junit.Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.isRunning());
        assertEquals(STATE_RUNNING, stopWatch.getRunningState());
    }

    /**
     * Test unsplit method with exception.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testUnsplitBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    /**
     * Test suspend method.
     */
    @org.junit.Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertFalse(stopWatch.isRunning());
        assertEquals(STATE_SUSPENDED, stopWatch.getRunningState());
    }

    /**
     * Test suspend method with exception.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    /**
     * Test resume method.
     */
    @org.junit.Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertEquals(STATE_RUNNING, stopWatch.getRunningState());
    }

    /**
     * Test resume method with exception.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    /**
     * Test getTime method.
     */
    @org.junit.Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long actual = stopWatch.getTime();
        long expected = stopWatch.getStartTime() - stopWatch.getStartTime();
        assertEquals(expected, actual);
    }

    /**
     * Test getSplitTime method.
     */
    @org.junit.Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        long actual = stopWatch.getSplitTime();
        long expected = stopWatch.getSplitStartTime() - stopWatch.getStartTime();
        assertEquals(expected, actual);
    }

    /**
     * Test toString method.
     */
    @org.junit.Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        String actual = stopWatch.toString();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        try {
            String expected = formatter.format(new java.util.Date(stopWatch.getTime()));
            assertEquals(expected, actual);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Test toSplitString method.
     */
    @org.junit.Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String actual = stopWatch.toSplitString();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        try {
            String expected = formatter.format(new java.util.Date(stopWatch.getSplitTime()));
            assertEquals(expected, actual);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }
}