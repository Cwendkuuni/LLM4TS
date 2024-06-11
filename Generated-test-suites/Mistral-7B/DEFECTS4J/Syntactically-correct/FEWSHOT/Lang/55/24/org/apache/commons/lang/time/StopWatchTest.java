package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testNewInstance() {
        StopWatch stopWatch = new StopWatch();
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals("0:00:00.000", stopWatch.toString());
        assertEquals("0:00:00.000", stopWatch.toSplitString());
    }

    @Test
    public void testStartStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertEquals(-1, time);
        stopWatch.reset();
        stopWatch.start();
        stopWatch.stop();
        time = stopWatch.getTime();
        assertNotNull(time);
        assertEquals(2, stopWatch.getRunningState());
        assertEquals(0, stopWatch.getSplitState());
        assertEquals("0:00:00.001+", stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        long splitTime = stopWatch.getTime();
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertEquals(-1, splitTime);
        assertEquals(11, stopWatch.getRunningState());
        assertEquals(11, stopWatch.getSplitState());
        assertEquals("0:00:00.001+", stopWatch.toString());
        assertEquals("0:00:0.001", stopWatch.toSplitString());
        stopWatch.stop();
        splitTime = stopWatch.getSplitTime();
        assertEquals(elapsedTime, splitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        assertSame(stopWatch.getStartTime(), time);
        assertEquals(10, stopWatch.getRunningState());
        assertEquals(0, stopWatch.getSplitState());
        assertEquals("0:00:00.001", stopWatch.toString());
        assertEquals("0:00:00.001", stopWatch.toSplitString());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test
    public void testSuspendResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - startTime;
        stopWatch.resume();
        startTime += pauseTime;
        long time = stopWatch.getTime();
        assertEquals(-1, time);
        assertEquals(12, stopWatch.getRunningState());
        assertEquals(0, stopWatch.getSplitState());
        assertEquals("0:00:00.002+", stopWatch.toString());
    }

    @Test
    public void testSuspendResumeTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime1 = System.currentTimeMillis() - startTime;
        stopWatch.resume();
        startTime += pauseTime1;
        stopWatch.suspend();
        long pauseTime2 = System.currenttimeMillis() - startTime;
        stopWatch.resume();
        startTime += pauseTime2;
        long time = stopWatch.getTime();
        assertEquals(-1, time);
        assertEquals(13, stopWatch.getRunningState());
        assertEquals(0, stopWatch.getSplitState());
        assertEquals("0:00:00.003+", stopWatch.toString());
    }

    @Test
    public void testSuspendResumeBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testSuspendResumeAfterStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - stopWatch.getStartTime();
        stopWatch.resume();
        long time = stopWatch.getTime();
        assertEquals(pauseTime, time);
        assertEquals(12, stopWatch.getRunningState());
        assertEquals(0, stopWatch.getSplitState());
        assertEquals("0:00:00.003", stopWatch.toString());
        assertEquals("0:00:00.003", stopWatch.toSplitString());
    }
}