package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.format.DurationFormatUtils;

/**
 * JUnit test cases for the StopWatch class
 *
 * @author John Doe
 */
public class StopWatchTest {

    @org.junit.Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    private StopWatch stopWatch;

    /**
     * Test method for {@link StopWatch#start()}.
     */
    @org.junit.Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());
    }

    /**
     * Test method for {@link StopWatch#stop()}.
     */
    @org.junit.Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(-1, stopWatch.getTime());
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());
    }

    /**
     * Test method for {@link StopWatch#reset()}.
     */
    @org.junit.Test
    public void testReset() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());
    }

    /**
     * Test method for {@link StopWatch#split()}.
     */
    @org.junit.Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.split(); // should throw an exception
        assertEquals(-1, stopWatch.getSplitTime());
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());
    }

    /**
     * Test method for {@link StopWatch#unsplit()}.
     */
    @org.junit.Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.unsplit();
        assertEquals(-1, stopWatch.getSplitTime());
        assertNull(stopWatch.getTime());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());
    }

    /**
     * Test method for {@link StopWatch#suspend()}.
     */
    @org.junit.Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.isSuspended());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
    }

    /**
     * Test method for {@link StopWatch#resume()}.
     */
    @org.junit.Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
    }

    /**
     * Test method for {@link StopWatch#getTime()}.
     */
    @org.junit.Test
    public void testGetTime() {
        stopWatch.start();
        Date start = new Date(System.currentTimeMillis());
        stopWatch.split();
        Date split = new Date(System.currentTimeMillis());
        stopWatch.stop();
        long expectedTotalTime = System.currentTimeMillis() - start.getTime();
        long expectedSplitTime = System.currentTimeMillis() - split.getTime();
        long totalTime = stopWatch.getTime();
        long splitTime = stopWatch.getSplitTime();

        assertEquals(expectedTotalTime, totalTime);
        assertEquals(expectedSplitTime, splitTime);
    }

    /**
     * Test method for {@link StopWatch#toString()}.
     */
    @org.junit.Test
    public void testToString() {
        stopWatch.start();
        Date start = new Date(System.currentTimeMillis());
        stopWatch.split();
        Date split = new Date(System.currentTimeMillis());
        stopWatch.stop();
        String expectedTotalString = DurationFormatUtils.formatDurationHMS(stopWatch.getTime());
        String expectedSplitString = DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime());
        String totalString = stopWatch.toString();
        String splitString = stopWatch.toSplitString();

        assertEquals(expectedTotalString, totalString);
        assertEquals(expectedSplitString, splitString);
    }

    /**
     * Test method for {@link StopWatch#start()}.
     * <p>
     * This tests the exception that should be thrown when starting a StopWatch
     * that is already running.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testStartAlreadyRunning() {
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test method for {@link StopWatch#split()}.
     * <p>
     * This tests the exception that should be thrown when splitting a StopWatch
     * that is not running.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSplitNotRunning() {
        stopWatch.split();
    }

    /**
     * Test method for {@link StopWatch#unsplit()}.
     * <p>
     * This tests the exception that should be thrown when unspliting a StopWatch
     * that has not been split.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testUnsplitNotSplit() {
        stopWatch.unsplit();
    }

    /**
     * Test method for {@link StopWatch#resume()}.
     * <p>
     * This tests the exception that should be thrown when resuming a StopWatch
     * that is not suspended.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testResumeNotSuspended() {
        stopWatch.resume();
    }

    /**
     * Test method for {@link StopWatch#suspend()}.
     * <p>
     * This tests the exception that should be thrown when suspending a StopWatch
     * that is not running.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSuspendNotRunning() {
        stopWatch.suspend();
    }

}