package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.text.ParseException;

public class StopWatchTest {

    @Test
    public void testInitialState() {
        StopWatch stopWatch = new StopWatch();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNull(stopWatch.getStartTime());
        assertNull(stopWatch.getStopTime());
        assertEquals("0:00:00.000", stopWatch.toString());
        assertEquals("0:00:00.000", stopWatch.toSplitString());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.hasSplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(System.currentTimeMillis(), stopWatch.getStopTime());
        assertEquals("0:00:01.000", stopWatch.toString());
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.hasSplit());
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long splitTime = stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(System.currentTimeMillis(), stopWatch.getStopTime());
        assertEquals("0:00:01.000", stopWatch.toString());
        assertEquals("0:00:01.000", stopWatch.toSplitString());
        assertEquals(splitTime, stopWatch.getSplitTime());
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.hasSplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis();
        stopWatch.resume();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertEquals(startTime, stopWatch.getStartTime());
        assertEquals(pauseTime, stopWatch.getStopTime());
        assertEquals("0:00:02.000", stopWatch.toString());
        assertEquals("0:00:01.000", stopWatch.toSplitString());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.hasSplit());
    }

    @Test
    public void testGetTimeAndSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long splitTime = stopWatch.split();
        stopWatch.stop();
        assertEquals(stopWatch.getTime(), stopWatch.getSplitTime());
        assertEquals("0:00:01.000", stopWatch.toString());
        assertEquals("0:00:01.000", stopWatch.toSplitString());
    }

    @Test
    public void testToStringAndToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long splitTime = stopWatch.split();
        stopWatch.stop();
        String toString = stopWatch.toString();
        String toSplitString = stopWatch.toSplitString();
        try {
            long time = Long.parseLong(toString.substring(toString.lastIndexOf(":") + 1));
            long splitTimeMillis = Long.parseLong(toSplitString.substring(toSplitString.lastIndexOf(":") + 1));
            assertEquals(time + splitTimeMillis, stopWatch.getTime());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.resume();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNull(stopWatch.getStartTime());
        assertNull(stopWatch.getStopTime());
        assertEquals("0:00:00.000", stopWatch.toString());
        assertEquals("0:00:00.000", stopWatch.toSplitString());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.hasSplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test(expected = RuntimeException.class)
    public void testIllegalRunningState() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getTime();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getSplitTime();
    }
}