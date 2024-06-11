package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;

import java.text.DurationFormatUtils;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testRunningStates() {
        StopWatch stopWatch = new StopWatch();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        long splitTime = System.currentTimeMillis();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertEquals(startTime, stopWatch.getStartTime());
        assertEquals(splitTime, stopWatch.getStopTime());
        assertEquals(splitTime - startTime, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        long splitTime = System.currentTimeMillis();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(startTime, stopWatch.getStartTime());
        assertNull(stopWatch.getStopTime());
        assertEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test
    public void testSuspendResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long suspendTime = System.currentTimeMillis();
        stopWatch.resume();
        long resumeTime = System.currentTimeMillis();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertEquals(startTime, stopWatch.getStartTime());
        assertEquals(resumeTime, stopWatch.getStopTime());
        assertEquals(resumeTime - startTime, stopWatch.getTime());
        assertEquals(suspendTime, stopWatch.getSuspendTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.stop();
        long stopTime = System.currentTimeMillis();
        assertEquals(stopTime - startTime, stopWatch.getTime());
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currenttimeMillis();
        stopWatch.split();
        long splitTime = System.currenttimeMillis();
        stopWatch.stop();
        assertEquals(splitTime - startTime, stopWatch.getSplitTime());
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currenttimeMillis();
        stopWatch.split();
        long splitTime = System.currenttimeMillis();
        stopWatch.stop();
        String result = stopWatch.toString();
        String expected = DurationFormatUtils.formatDurationHMS(startTime);
        String expectedSplit = DurationFormatUtils.formatDurationHMS(splitTime - startTime);
        assertTrue(result.startsWith(expected + " - " + expectedSplit));
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currenttimeMillis();
        stopWatch.split();
        long splitTime = System.currenttimeMillis();
        stopWatch.stop();
        String result = stopWatch.toSplitString();
        String expected = DurationFormatUtils.formatDurationHMS(splitTime - startTime);
        assertEquals(expected, result);
    }
}