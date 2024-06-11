package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;

import java.text.DurationFormatUtils;

public class StopWatchTest {

    @org.junit.Test
    public void testConstructor() {
        new StopWatch();
    }

    @org.junit.Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.canStart());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());

        stopWatch.stop();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.canStart());
        assertEquals(-1, stopWatch.getTime());
        assertEquals(-1, stopWatch.getSplitTime());
        assertNotNull(stopWatch.toString());
        assertNotNull(stopWatch.toSplitString());
    }

    @org.junit.Test
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
        assertThrows("IllegalStateException", () -> stopWatch.start());
    }

    @org.junit.Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.canSplit());
        assertNotNull(stopWatch.getTime());
        assertEquals(-1, stopWatch.getSplitTime());
        assertNotNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());

        stopWatch.stop();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.canSplit());
        assertEquals(-1, stopWatch.getTime());
        assertEquals(stopWatch.getTime(), stopWatch.getSplitTime());
        assertNotNull(stopWatch.toString());
        assertNotNull(stopWatch.toSplitString());
    }

    @org.junit.Test
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
        assertThrows("IllegalStateException", () -> stopWatch.split());
    }

    @org.junit.Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.canResume());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());

        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.canResume());
        assertEquals(-1, stopWatch.getTime());
        assertEquals(-1, stopWatch.getSplitTime());
        assertNotNull(stopWatch.toString());
        assertNotNull(stopWatch.toSplitString());
    }

    @org.junit.Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.canResume());
        assertNull(stopWatch.getTime());
        assertEquals(-1, stopWatch.getSplitTime());
        assertNotNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());

        stopWatch.stop();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.canResume());
        assertEquals(-1, stopWatch.getTime());
        assertEquals(-1, stopWatch.getSplitTime());
        assertNotNull(stopWatch.toString());
        assertNotNull(stopWatch.toSplitString());
    }

    @org.junit.Test
    public void testResumeTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertThrows("IllegalStateException", () -> stopWatch.resume());
    }

    @org.junit.Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.canStart());
        assertEquals(-1, stopWatch.getTime());
        assertEquals(-1, stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());

        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.canStart());
        assertEquals(-1, stopWatch.getTime());
        assertEquals(stopWatch.getTime(), stopWatch.getSplitTime());
        assertNotNull(stopWatch.toString());
        assertNotNull(stopWatch.toSplitString());
    }

    @org.junit.Test
    public void testUnsplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.unsplit();
        assertThrows("IllegalStateException", () -> stopWatch.unsplit());
    }

    @org.junit.Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.canStart());
        assertEquals(-1, stopWatch.getTime());
        assertEquals(-1, stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    @org.junit.Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(stopWatch.getTime(), stopWatch.getSplitTime());
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getTime()), stopWatch.toString());
    }

    @org.junit.Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertEquals(stopWatch.getSplitTime(), stopWatch.getTime() - stopWatch.getStartTime());
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()), stopWatch.toSplitString());
    }

    @org.junit.Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getTime()), stopWatch.toString());
    }

    @org.junit.Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()), stopWatch.toSplitString());
    }
}