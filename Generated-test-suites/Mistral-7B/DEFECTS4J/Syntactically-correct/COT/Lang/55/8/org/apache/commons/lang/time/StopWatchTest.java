package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Test class for StopWatch.
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
        stopWatch.stop();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_STOPPED);
        assertNotEquals(-1, stopWatch.getTime());
    }

    /**
     * Test method for {@link StopWatch#reset()}.
     */
    @org.junit.Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_UNSTARTED);
        assertEquals(-1, stopWatch.getTime());
    }

    /**
     * Test method for {@link StopWatch#split()}.
     */
    @org.junit.Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_RUNNING);
        assertTrue(stopWatch.getSplitState() == StopWatch.STATE_SPLIT);
        assertNotEquals(-1, stopWatch.getSplitTime());
    }

    /**
     * Test method for {@link StopWatch#unsplit()}.
     */
    @org.junit.Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.getRunningState() == StopWatch.STATE_RUNNING);
        assertTrue(stopWatch.getSplitState() == StopWatch.STATE_UNSPLIT);
        assertNotEquals(-1, stopWatch.getTime());
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
     * Test method for {@link StopWatch#getTime()}.
     */
    @org.junit.Test
    public void testGetTime() {
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
        try {
            long expected = Long.parseLong(format.format(new java.util.Date(time)));
            long splitTime = stopWatch.getSplitTime();
            SimpleDateFormat splitFormat = new SimpleDateFormat("HH:mm:ss.SSS");
            try {
                long expectedSplit = Long.parseLong(splitFormat.format(new java.util.Date(splitTime)));
                assertEquals(expected + expectedSplit, stopWatch.getTotalTime());
            } catch (ParseException e) {
                fail("Failed to parse split time: " + e.getMessage());
            }
        } catch (ParseException e) {
            fail("Failed to parse time: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link StopWatch#getSplitTime()}.
     */
    @org.junit.Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
        try {
            long expected = Long.parseLong(format.format(new java.util.Date(splitTime)));
            assertEquals(expected, stopWatch.getSplitTime());
        } catch (ParseException e) {
            fail("Failed to parse split time: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link StopWatch#toString()}.
     */
    @org.junit.Test
    public void testToString() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String timeString = stopWatch.toString();
        String splitString = stopWatch.toSplitString();
        String totalString = stopWatch.toString();
        assertTrue(timeString.contains(":"));
        assertTrue(splitString.contains(":"));
        assertTrue(totalString.contains(":"));
        // TODO: Implement better assertion for total string
        //assertTrue(totalString.contains(timeString + " + " + splitString));
    }
}