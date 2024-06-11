package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;

public class StopWatchTest {

    @Test
    public void testConstructor() {
        new StopWatch();
    }

    @Test
    public void testIllegalStateException_startTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertThrows(IllegalStateException.class, stopWatch::start);
    }

    @Test
    public void testIllegalStateException_splitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertThrows(IllegalStateException.class, stopWatch::split);
    }

    @Test
    public void testIllegalStateException_suspendTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertThrows(IllegalStateException.class, stopWatch::suspend);
    }

    @Test
    public void testIllegalStateException_resumeTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertThrows(IllegalStateException.class, stopWatch::resume);
    }

    @Test
    public void testIllegalStateException_resetTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.reset();
        assertThrows(IllegalStateException.class, stopWatch::reset);
    }

    @Test
    public void testIllegalStateException_startBeforeReset() {
        StopWatch stopWatch = new StopWatch();
        assertThrows(IllegalStateException.class, stopWatch::start);
    }

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.isSuspended());
        assertFalse(stopWatch.isRunning());
    }

    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
    }

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.isStopped());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        assertEquals(-1, stopWatch.getTime());
        assertEquals(0, stopWatch.getSplitTime());
        assertNotNull(stopWatch.getStartTime());
        assertNotNull(stopWatch.getSplitTime());
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertEquals(0, stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        stopWatch.stop();
        assertEquals(time, stopWatch.getTime());
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        assertEquals(splitTime, stopWatch.getSplitTime());
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String toString = stopWatch.toString();
        assertEquals("0s", toString);
        stopWatch.stop();
        toString = stopWatch.toString();
        Duration duration = Duration.ofMillis(stopWatch.getTime());
        String formattedDuration = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals(formattedDuration, toString);
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String toSplitString = stopWatch.toSplitString();
        assertEquals("0s", toSplitString);
        stopWatch.stop();
        long time = stopWatch.getTime();
        stopWatch.reset();
        assertEquals("0s", toSplitString);
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        Duration duration = Duration.ofMillis(splitTime);
        String formattedDuration = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals(formattedDuration, toSplitString);
    }

}