package org.apache.commons.lang.time;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class contains JUnit test cases for the StopWatch class.
 */
public class StopWatchTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        new StopWatch();
    }

    /**
     * Test start() method.
     */
    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // check that startTime is set
        Assert.assertNotEquals(-1, stopWatch.getTime());
    }

    /**
     * Test stop() method.
     */
    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        // check that stopTime is set
        Assert.assertNotEquals(-1, stopWatch.getTime());
    }

    /**
     * Test reset() method.
     */
    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        // check that startTime and stopTime are reset
        Assert.assertEquals(-1, stopWatch.getTime());
    }

    /**
     * Test split() method.
     */
    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        // check that splitState is set and startTime and stopTime are not reset
        Assert.assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        Assert.assertNotEquals(-1, stopWatch.getTime());
        Assert.assertNotEquals(-1, stopWatch.getSplitTime());
    }

    /**
     * Test unsplit() method.
     */
    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        // check that splitState is reset and startTime and stopTime are not changed
        Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        Assert.assertNotEquals(-1, stopWatch.getTime());
        Assert.assertNotEquals(-1, stopWatch.getSplitTime());
    }

    /**
     * Test suspend() method.
     */
    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        // check that stopTime is set and runningState is suspended
        Assert.assertNotEquals(-1, stopWatch.getTime());
        Assert.assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    /**
     * Test resume() method.
     */
    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        // check that startTime is updated and runningState is running
        Assert.assertNotEquals(-1, stopWatch.getTime());
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    /**
     * Test getTime() method.
     */
    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        stopWatch.stop();
        // check that getTime() returns the correct time
        Assert.assertEquals(time, stopWatch.getTime());
    }

    /**
     * Test getSplitTime() method.
     */
    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.stop();
        // check that getSplitTime() returns the correct split time
        Assert.assertEquals(splitTime, stopWatch.getSplitTime());
    }

    /**
     * Test toString() method.
     */
    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String toString = stopWatch.toString();
        stopWatch.stop();
        // check that toString() returns a string representation of the time
        Assert.assertNotNull(toString);
    }

    /**
     * Test toSplitString() method.
     */
    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String toSplitString = stopWatch.toSplitString();
        stopWatch.stop();
        // check that toSplitString() returns a string representation of the split time
        Assert.assertNotNull(toSplitString);
    }

    /**
     * Test illegal call to start() method twice.
     */
    @Test(expected = IllegalStateException.class)
    public void testIllegalStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test illegal call to split() method twice.
     */
    @Test(expected = IllegalStateException.class)
    public void testIllegalSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    /**
     * Test illegal call to unsplit() method without a split.
     */
    @Test(expected = IllegalStateException.class)
    public void testIllegalUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.unsplit();
    }

    /**
     * Test illegal call to resume() method without a suspend.
     */
    @Test(expected = IllegalStateException.class)
    public void testIllegalResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.resume();
    }
}