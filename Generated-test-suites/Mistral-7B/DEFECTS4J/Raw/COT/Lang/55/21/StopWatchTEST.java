package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;
import java.text.ParsePosition;

/**
 * Test class for StopWatch
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
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_RUNNING);
    }

    /**
     * Test method for {@link StopWatch#stop()}.
     */
    @org.junit.Test
    public void testStop() {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertTrue(time > 0);
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_STOPPED);
        long elapsedTime = System.currentTimeMillis() - startTime - time;
        assertEquals(elapsedTime, 0);
    }

    /**
     * Test method for {@link StopWatch#reset()}.
     */
    @org.junit.Test
    public void testReset() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    /**
     * Test method for {@link StopWatch#split()}.
     */
    @org.junit.Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitState() == StopWatch.STATE_SPLIT);
    }

    /**
     * Test method for {@link StopWatch#unsplit()}.
     */
    @org.junit.Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(0, stopWatch.getSplitTime());
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    /**
     * Test method for {@link StopWatch#suspend()}.
     */
    @org.junit.Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_SUSPENDED);
    }

    /**
     * Test method for {@link StopWatch#resume()}.
     */
    @org.junit.Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_RUNNING);
    }

    /**
     * Test method for {@link StopWatch#toString()}.
     */
    @org.junit.Test
    public void testToString() {
        stopWatch.start();
        stopWatch.stop();
        String timeString = stopWatch.toString();
        ParsePosition parsePosition = new ParsePosition(0);
        long time = 0;
        try {
            time = Long.parseLong(timeString.substring(0, timeString.lastIndexOf('.')));
        } catch (NumberFormatException e) {
            fail("Invalid string format: " + timeString);
        }
        StopWatch newStopWatch = new StopWatch();
        newStopWatch.start();
        long elapsedTime = System.currentTimeMillis() - time;
        String newTimeString = newStopWatch.toString();
        long newTime = 0;
        try {
            newTime = Long.parseLong(newTimeString);
        } catch (NumberFormatException e) {
            fail("Invalid string format: " + newTimeString);
        }
        assertEquals(elapsedTime, newTime);
    }

    /**
     * Test method for {@link StopWatch#getTime()}.
     */
    @org.junit.Test
    public void testGetTime() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertTrue(time > 0);
        stopWatch.reset();
        time = stopWatch.getTime();
        assertEquals(0, time);
    }

    /**
     * Test method for {@link StopWatch#getSplitTime()}.
     */
    @org.junit.Test
    public void testGetSplitTime() throws ParseException {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertTrue(time > 0);
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime > 0);
        stopWatch.reset();
        splitTime = stopWatch.getSplitTime();
        assertEquals(0, splitTime);
    }

    /**
     * Test method for IllegalStateException when attempting to start a StopWatch when it is already running.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenStartingAStopWatchWhenItIsAlreadyRunning() {
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test method for IllegalStateException when attempting to split a StopWatch when it is not running.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenSplittingAStopWatchWhenItIsNotRunning() {
        stopWatch.split();
    }

    /**
     * Test method for IllegalStateException when attempting to suspend a StopWatch when it is not running.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenSuspendingAStopWatchWhenItIsNotRunning() {
        stopWatch.suspend();
    }

    /**
     * Test method for IllegalStateException when attempting to resume a StopWatch when it is not suspended.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenResumingAStopWatchWhenItIsNotSuspended() {
        stopWatch.resume();
    }

    /**
     * Test method for IllegalStateException when attempting to unsplit a StopWatch when it has not been split.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenUnsplittingAStopWatchWhenItHasNotBeenSplit() {
        stopWatch.unsplit();
    }
}