package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;

public class StopWatchTest {

    @Test
    public void testStartStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        assertEquals(Duration.ZERO, Duration.ofMillis(stopwatch.getTime()));
    }

    @Test
    public void testStartSplitStart() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.start();
        stopwatch.split();
        stopwatch.stop();
        assertEquals(Duration.ofMillis(3), Duration.ofMillis(stopwatch.getTime()));
        assertEquals(Duration.ofMillis(1), Duration.ofMillis(stopwatch.getSplitTime()));
        assertEquals(Duration.ofMillis(2), Duration.ofMillis(stopwatch.getSplitTime()));
    }

    @Test
    public void testStartSplitStartSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.start();
        stopwatch.split();
        stopwatch.stop();
        assertEquals(Duration.ofMillis(4), Duration.ofMillis(stopwatch.getTime()));
        assertEquals(Duration.ofMillis(1), Duration.ofMillis(stopwatch.getSplitTime()));
        assertEquals(Duration.ofMillis(1), Duration.ofMillis(stopwatch.getSplitTime()));
        assertEquals(Duration.ofMillis(2), Duration.ofMillis(stopwatch.getSplitTime()));
    }

    @Test
    public void testStartSplitStartSplitStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.start();
        stopwatch.split();
        stopwatch.split();
        stopwatch.stop();
        assertEquals(Duration.ofMillis(6), Duration.ofMillis(stopwatch.getTime()));
        assertEquals(Duration.ofMillis(1), Duration.ofMillis(stopwatch.getSplitTime()));
        assertEquals(Duration.ofMillis(1), Duration.ofMillis(stopwatch.getSplitTime()));
        assertEquals(Duration.ofMillis(1), Duration.ofMillis(stopwatch.getSplitTime()));
    }

    @Test
    public void testSplitUnsplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.stop();
        assertEquals(Duration.ofMillis(3), Duration.ofMillis(stopwatch.getTime()));
        assertNull(stopwatch.getSplitTime());
    }

    @Test
    public void testStartSplitSuspendResume() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.suspend();
        stopwatch.resume();
        stopwatch.stop();
        assertEquals(Duration.ofMillis(5), Duration.ofMillis(stopwatch.getTime()));
        assertEquals(Duration.ofMillis(1), Duration.ofMillis(stopwatch.getSplitTime()));
    }

    @Test
    public void testStartSplitSuspendResumeSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.suspend();
        stopwatch.resume();
        stopwatch.split();
        stopwatch.stop();
        assertEquals(Duration.ofMillis(7), Duration.ofMillis(stopwatch.getTime()));
        assertEquals(Duration.ofMillis(1), Duration.ofMillis(stopwatch.getSplitTime()));
        assertEquals(Duration.ofMillis(1), Duration.ofMillis(stopwatch.getSplitTime()));
    }

    @Test
    public void testSuspendResume() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        stopwatch.stop();
        assertEquals(Duration.ofMillis(3), Duration.ofMillis(stopwatch.getTime()));
    }

    @Test
    public void testStartSuspendResume() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        stopwatch.stop();
        assertEquals(Duration.ofMillis(6), Duration.ofMillis(stopwatch.getTime()));
    }

    @Test
    public void testStopwatchIsImmutable() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.setRunningState(StopWatch.STATE_STOPPED); // not a public method
        assertFalse(stopwatch.isRunning());
        stopwatch.stop();
        assertTrue(stopwatch.isRunning());
    }

    @Test
    public void testToString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        assertEquals("0:0:0.001", stopwatch.toString());
    }

    @Test
    public void testToSplitString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.stop();
        assertEquals("0:0:0.001", stopwatch.toSplitString());
    }
}