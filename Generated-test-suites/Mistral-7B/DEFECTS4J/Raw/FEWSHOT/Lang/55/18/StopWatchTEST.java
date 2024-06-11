package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testUnstartedStopWatch() {
        StopWatch stopWatch = new StopWatch();
        assertFalse(stopWatch.isRunning());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals("0:00:00.000", stopWatch.toString());
        assertEquals("0:00:00.000", stopWatch.toSplitString());
        assertNull(stopWatch.startTime);
        assertNull(stopWatch.stopTime);
    }

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(1, stopWatch.runningState);
        assertEquals(0, stopWatch.splitState);
        assertNotNull(stopWatch.startTime);
        assertNotNull(stopWatch.stopTime);
        assertEquals(stopWatch.stopTime - stopWatch.startTime, stopWatch.getTime());
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getTime()), stopWatch.toString());
        assertEquals("0:00:00.000", stopWatch.toSplitString());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(1, stopWatch.runningState);
        assertEquals(0, stopWatch.splitState);
        assertNotNull(stopWatch.startTime);
        assertNotNull(stopWatch.stopTime);
        assertEquals(stopWatch.stopTime - stopWatch.startTime, stopWatch.getTime());
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getTime()), stopWatch.toString());
        assertEquals("0:00:00.000", stopWatch.toSplitString());
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
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.stop();
        assertEquals(1, stopWatch.runningState);
        assertEquals(0, stopWatch.splitState);
        assertNotNull(stopWatch.startTime);
        assertNotNull(stopWatch.stopTime);
        long timeBetweenSuspendAndResume = stopWatch.stopTime - stopWatch.startTime - stopWatch.getSplitTime();
        assertEquals(timeBetweenSuspendAndResume, stopWatch.getTime());
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getTime()), stopWatch.toString());
        assertEquals("0:00:00.000", stopWatch.toSplitString());
    }

}