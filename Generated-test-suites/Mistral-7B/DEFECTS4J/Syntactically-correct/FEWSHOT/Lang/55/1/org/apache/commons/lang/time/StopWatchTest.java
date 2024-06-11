package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(0L, stopWatch.getTime());
        assertEquals(StringUtils.isBlank(stopWatch.toString()), true);
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        assertEquals(0L, stopWatch.getTime());
        assertEquals(0L, stopWatch.getSplitTime());
        assertFalse(StringUtils.isBlank(stopWatch.toString()));
        assertFalse(StringUtils.isBlank(stopWatch.toSplitString()));
        stopWatch.unsplit();
        assertEquals(0L, stopWatch.getSplitTime());
        assertSame(stopWatch.toString(), stopWatch.toSplitString());
        stopWatch.reset();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.stop();
        assertEquals(0L, stopWatch.getTime());
        assertEquals(StringUtils.isBlank(stopWatch.toString()), true);
    }

    @Test
    public void testSuspendAndResumeMultipleTimes() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.stop();
        assertEquals(0L, stopWatch.getTime());
        assertEquals(StringUtils.isBlank(stopWatch.toString()), true);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testStartAndSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        assertEquals(-1L, stopWatch.getTime());
        assertEquals(0L, stopWatch.getSplitTime());
        assertFalse(StringUtils.isBlank(stopWatch.toString()));
        assertFalse(StringUtils.isBlank(stopWatch.toSplitString()));
    }

    @Test
    public void testStartAndSplitMultipleTimes() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
        stopWatch.split();
        stopWatch.stop();
        assertEquals(-1L, stopWatch.getTime());
        assertEquals(DurationFormatUtils.parseDuration("PT1.001S"), stopWatch.getSplitTime());
        assertFalse(StringUtils.isBlank(stopWatch.toString()));
        assertFalse(StringUtils.isBlank(stopWatch.toSplitString()));
    }

    @Test
    public void testSplitAndSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.stop();
        assertEquals(0L, stopWatch.getTime());
        assertEquals(0L, stopWatch.getSplitTime());
        assertFalse(StringUtils.isBlank(stopWatch.toString()));
        assertFalse(StringUtils.isBlank(stopWatch.toSplitString()));
    }

    @Test
    public void testSplitAndSuspendMultipleTimes() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.split();
        stopWatch.split();
        stopWatch.stop();
        assertEquals(0L, stopWatch.getTime());
        assertEquals(DurationFormatUtils.parseDuration("PT1.061S"), stopWatch.getSplitTime());
        assertEquals(DurationFormatUtils.parseDuration("PT0.5S"), stopWatch.getSplitTime() + stopWatch.getSplitTime());
        assertFalse(StringUtils.isBlank(stopWatch.toString()));
        assertFalse(StringUtils.isBlank(stopWatch.toSplitString()));
    }

    @Test
    public void testGetTimeAfterStartButBeforeStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        stopWatch.stop();
        assertEquals(time, stopWatch.getTime());
        assertEquals(DurationFormatUtils.parseDuration("PT" + Long.toString(time) + "MS"), stopWatch.toString());
    }

    @Test
    public void testGetSplitTimeAfterSplitButBeforeStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.stop();
        assertEquals(splitTime, stopWatch.getSplitTime());
        assertEquals(DurationFormatUtils.parseDuration("PT" + Long.toString(splitTime) + "MS"), stopWatch.toSplitString());
    }

    @Test
    public void testGetTimeAfterStartButBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        stopWatch.split();
        stopWatch.stop();
        assertEquals(time + stopWatch.getSplitTime(), stopWatch.getTime());
        assertEquals(DurationFormatUtils.parseDuration("PT" + Long.toString(time + stopWatch.getSplitTime()) + "MS"), stopWatch.toString());
    }

    @Test
    public void testGetSplitTimeAfterStartButBeforeStopWithMultipleSplits() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.stop();
        assertEquals(splitTime + stopWatch.getSplitTime(), stopWatch.getTime());
        assertEquals(DurationFormatUtils.parseDuration("PT" + Long.toString(splitTime + stopWatch.getSplitTime()) + "MS"), stopWatch.toString());
    }

    @Test
    public void testGetTimeAfterSplitButBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
        long time = stopWatch.getTime();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(time + stopWatch.getSplitTime(), stopWatch.getTime());
        assertEquals(DurationFormatUtils.parseDuration("PT" + Long.toString(time + stopWatch.getSplitTime()) + "MS"), stopWatch.toString());
    }

    @Test
    public void testGetSplitTimeAfterSplitButBeforeStartWithMultipleSplits() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(splitTime + stopWatch.getSplitTime(), stopWatch.getTime());
        assertEquals(DurationFormatUtils.parseDuration("PT" + Long.toString(splitTime + stopWatch.getSplitTime()) + "MS"), stopWatch.toString());
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(-1L, stopWatch.getTime());
        assertEquals(-1L, stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }
}