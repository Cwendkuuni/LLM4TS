package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * Test method start.
     */
    @org.junit.Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_RUNNING);
    }

    /**
     * Test method start with already running stopwatch.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testStartAlreadyRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test method stop.
     */
    @org.junit.Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_STOPPED);
    }

    /**
     * Test method stop with not running stopwatch.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testStopNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    /**
     * Test method reset.
     */
    @org.junit.Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_UNSTARTED);
    }

    /**
     * Test method split.
     */
    @org.junit.Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_RUNNING);
    }

    /**
     * Test method split with not running stopwatch.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSplitNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    /**
     * Test method unsplit.
     */
    @org.junit.Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_RUNNING);
    }

    /**
     * Test method unsplit with not split stopwatch.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testUnsplitNotSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    /**
     * Test method suspend.
     */
    @org.junit.Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_SUSPENDED);
    }

    /**
     * Test method suspend with not running stopwatch.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSuspendNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    /**
     * Test method resume.
     */
    @org.junit.Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_RUNNING);
    }

    /**
     * Test method resume with not suspended stopwatch.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testResumeNotSuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    /**
     * Test method getTime.
     */
    @org.junit.Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        assertTrue(time >= 0);
    }

    /**
     * Test method getSplitTime.
     */
    @org.junit.Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime >= 0);
    }

    /**
     * Test method toString.
     */
    @org.junit.Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String toString = stopWatch.toString();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date;
        try {
            date = format.parse(toString);
        } catch (ParseException e) {
            fail("toString returned invalid format: " + toString);
        }
        long time = date.getTime();
        assertTrue(time >= 0);
    }

    /**
     * Test method toSplitString.
     */
    @org.junit.Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String toSplitString = stopWatch.toSplitString();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date;
        try {
            date = format.parse(toSplitString);
        } catch (ParseException e) {
            fail("toSplitString returned invalid format: " + toSplitString);
        }
        long time = date.getTime();
        assertTrue(time >= 0);
    }
}