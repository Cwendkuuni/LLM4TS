package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.StopWatch;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertTrue(stopWatch.getTime() == 0);
    }

    @Test
    public void testSplitUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testSplitGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertEquals(splitTime, stopWatch.getTime());
        stopWatch.unsplit();
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testSuspendResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long initialTime = stopWatch.getTime();
        stopWatch.suspend();
        long suspendedTime = stopWatch.getTime();
        stopWatch.resume();
        long resumedTime = stopWatch.getTime();
        assertEquals(initialTime + suspendedTime - resumedTime, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.yield();
        Thread.yield();
        stopWatch.stop();
        String toString = stopWatch.toString();
        String expected = DurationFormatUtils.formatDurationHMS(stopWatch.getTime());
        assertEquals(expected, toString);
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.yield();
        Thread.yield();
        stopWatch.split();
        Thread.yield();
        Thread.yield();
        stopWatch.stop();
        String toSplitString = stopWatch.toSplitString();
        String expected = DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime());
        assertEquals(expected, toSplitString);
    }

    @Test
    public void testGetTimeBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testGetTimeDuringStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long timeBefore = System.currentTimeMillis();
        Thread.yield();
        Thread.yield();
        long timeAfter = System.currentTimeMillis();
        long elapsedTime = timeAfter - timeBefore;
        assertEquals(elapsedTime, stopWatch.getTime());
        stopWatch.stop();
    }

    @Test
    public void testGetTimeAfterStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long timeBefore = System.currentTimeMillis();
        long timeAfter = System.currentTimeMillis();
        long elapsedTime = timeAfter - timeBefore;
        assertEquals(stopWatch.getTime(), elapsedTime);
    }

    @Test
    public void testGetSplitTimeBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        assertEquals(0, stopWatch.getSplitTime());
    }

    @Test
    public void testGetSplitTimeDuringSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long timeBefore = System.currentTimeMillis();
        Thread.yield();
        Thread.yield();
        long timeAfter = System.currentTimeMillis();
        long elapsedTime = timeAfter - timeBefore;
        assertEquals(elapsedTime, stopWatch.getSplitTime());
        stopWatch.unsplit();
    }

    @Test
    public void testGetSplitTimeAfterSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        long timeBefore = System.currenttimeMillis();
        long timeAfter = System.currenttimeMillis();
        long elapsedTime = timeAfter - timeBefore;
        assertEquals(stopWatch.getSplitTime(), elapsedTime);
    }

    @Test
    public void testToStringBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        assertEquals("0:00:00.000", stopWatch.toString());
    }

    @Test
    public void testToStringDuringStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.yield();
        Thread.yield();
        String toString = stopWatch.toString();
        String expected = DurationFormatUtils.formatDurationHMS(System.currentTimeMillis() - stopWatch.getStartTime());
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringAfterStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        String toString = stopWatch.toString();
        String expected = DurationFormatUtils.formatDurationHMS(stopWatch.getTime());
        assertEquals(expected, toString);
    }

    @Test
    public void testToSplitStringBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        assertEquals("0:00:00.000", stopWatch.toSplitString());
    }

    @Test
    public void testToSplitStringDuringSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        Thread.yield();
        Thread.yield();
        String toSplitString = stopWatch.toSplitString();
        String expected = DurationFormatUtils.formatDurationHMS(System.currentTimeMillis() - stopWatch.getStartTime());
        assertEquals(expected, toSplitString);
    }

    @Test
    public void testToSplitStringAfterSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String toSplitString = stopWatch.toSplitString();
        String expected = DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime());
        assertEquals(expected, toSplitString);
    }

    @Test
    public void testToStringAndToSplitStringAfterReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals("0:00:00.000", stopWatch.toString());
        assertEquals("0:00:00.000", stopWatch.toSplitString());
    }
}