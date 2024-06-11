package org.apache.commons.lang.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for StopWatch.
 */
public class StopWatchTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        StopWatch stopWatch = new StopWatch();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
    }

    /**
     * Test start method.
     */
    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());

        long startTime = System.currentTimeMillis();
        stopWatch.start();
        long elapsedTime = stopWatch.getTime();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(elapsedTime, stopWatch.getTime());
    }

    /**
     * Test start method with previous start.
     */
    @Test(expected = IllegalStateException.class)
    public void testStartWithPreviousStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test split method.
     */
    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        long timeBeforeSplit = System.currentTimeMillis();
        stopWatch.split();

        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(timeBeforeSplit - stopWatch.getStartTime(), stopWatch.getSplitTime());
        assertEquals(stopWatch.getTime(), timeBeforeSplit - stopWatch.getStartTime());

        long elapsedTime = stopWatch.getTime();
        stopWatch.stop();

        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        assertEquals(stopWatch.getStartTime(), stopWatch.getStartTime());
        assertEquals(elapsedTime, stopWatch.getStopTime() - stopWatch.getStartTime());
        assertEquals(elapsedTime, stopWatch.getSplitTime());
        assertEquals(elapsedTime, stopWatch.getTime());
    }

    /**
     * Test split method with previous split.
     */
    @Test(expected = IllegalStateException.class)
    public void testSplitWithPreviousSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    /**
     * Test unsplit method.
     */
    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        long timeBeforeSplit = System.currentTimeMillis();
        stopWatch.split();

        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(timeBeforeSplit - stopWatch.getStartTime(), stopWatch.getSplitTime());
        assertEquals(stopWatch.getTime(), timeBeforeSplit - stopWatch.getStartTime());

        stopWatch.unsplit();

        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(stopWatch.getTime(), timeBeforeSplit - stopWatch.getStartTime());
    }

    /**
     * Test unsplit method with no split.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithNoSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.unsplit();
    }

    /**
     * Test resume method.
     */
    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();

        long timeBeforeResume = System.currentTimeMillis();
        stopWatch.resume();

        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(timeBeforeResume - stopWatch.getStartTime(), stopWatch.getTime());
        assertEquals(timeBeforeResume - stopWatch.getStartTime(), stopWatch.getSplitTime());

        long elapsedTime = stopWatch.getTime();
        stopWatch.stop();

        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getSplitState());
        assertEquals(stopWatch.getStartTime(), stopWatch.getStartTime());
        assertEquals(elapsedTime, stopWatch.getStopTime() - stopWatch.getStartTime());
        assertEquals(elapsedTime, stopWatch.getSplitTime());
        assertEquals(elapsedTime, stopWatch.getTime());
    }

    /**
     * Test resume method with no suspend.
     */
    @Test(expected = IllegalStateException.class)
    public void testResumeWithNoSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.resume();
    }

    /**
     * Test suspend method.
     */
    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        long timeBeforeSuspend = System.currentTimeMillis();
        stopWatch.suspend();

        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(timeBeforeSuspend - stopWatch.getStartTime(), stopWatch.getTime());
        assertEquals(timeBeforeSuspend - stopWatch.getStartTime(), stopWatch.getSplitTime());

        stopWatch.resume();

        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(stopWatch.getTime(), timeBeforeSuspend - stopWatch.getStartTime());
    }

    /**
     * Test suspend method with no running.
     */
    @Test(expected = IllegalStateException.class)
    public void testSuspendWithNoRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }
}