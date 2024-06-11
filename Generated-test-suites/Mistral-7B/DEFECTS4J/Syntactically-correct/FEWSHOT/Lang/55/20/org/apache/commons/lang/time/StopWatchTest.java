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
    public void testStateUnstarted() {
        StopWatch stopWatch = new StopWatch();
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals("0:00:00.000", stopWatch.toString());
        assertEquals("0:00:00.000", stopWatch.toSplitString());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
    }

    @Test
    public void testStartStop() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        stopWatch.stop();
        assertEquals(1000L, stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals("0:00:01.000", stopWatch.toString());
        assertEquals("0:00:01.000", stopWatch.toSplitString());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        stopWatch.reset();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitAndStop() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        stopWatch.split();
        Thread.sleep(1000);
        stopWatch.stop();
        assertEquals(2000L, stopWatch.getTime());
        assertEquals(1000L, stopWatch.getSplitTime());
        assertEquals("0:00:02.000", stopWatch.toString());
        assertEquals("0:00:01.000", stopWatch.toSplitString());
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        stopWatch.reset();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testSuspendResume() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        stopWatch.suspend();
        Thread.sleep(1000);
        stopWatch.resume();
        Thread.sleep(1000);
        stopWatch.stop();
        assertEquals(3000L, stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals("0:00:03.000", stopWatch.toString());
        assertEquals("0:00:03.000", stopWatch.toSplitString());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        stopWatch.reset();
    }

    @Test
    public void testSuspendTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(2000L, stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals("0:00:02.000", stopWatch.toString());
        assertEquals("0:00:02.000", stopWatch.toSplitString());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        stopWatch.reset();
    }

    @Test
    public void testSplitThenSuspend() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        Thread.sleep(1000);
        stopWatch.suspend();
        Thread.sleep(1000);
        stopWatch.resume();
        Thread.sleep(1000);
        stopWatch.stop();
        assertEquals(3000L, stopWatch.getTime());
        assertEquals(1000L, stopWatch.getSplitTime());
        assertEquals("0:00:03.000", stopWatch.toString());
        assertEquals("0:00:01.000", stopWatch.toSplitString());
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        stopWatch.reset();
    }

    @Test
    public void testSplitThenStop() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        Thread.sleep(1000);
        stopWatch.stop();
        assertEquals(1000L, stopWatch.getTime());
        assertEquals(0L, stopWatch.getSplitTime());
        assertEquals("0:00:01.000", stopWatch.toString());
        assertEquals("0:00:01.000", stopWatch.toSplitString());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        stopWatch.reset();
    }

    @Test
    public void testToStringAndToSplitString() throws ParseException {
        StopWatch stopWatch = new StopWatch();
        long time = 1234567L;
        stopWatch.start();
        Thread.sleep(time);
        stopWatch.split();
        Thread.sleep(time);
        stopWatch.stop();
        String expected = DurationFormatUtils.formatDurationHMS(time * 2);
        assertEquals(expected, stopWatch.toString());
        String expectedSplit = DurationFormatUtils.formatDurationHMS(time);
        assertEquals(expectedSplit, stopWatch.toSplitString());
    }
}