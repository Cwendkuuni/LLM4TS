package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.ParseException;

public class StopWatchTest {

    @Test
    public void testStopWatch() {
        StopWatch stopWatch = new StopWatch();

        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals("0:00:00.000", stopWatch.toString());

        stopWatch.start();

        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());

        stopWatch.stop();

        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertTrue(stopWatch.isSplit());
        assertTrue(stopWatch.isStopped());

        long time = stopWatch.getTime();
        assertEquals(time, 0);

        assertEquals("0:00:00.000", stopWatch.toString());

        // Test split
        stopWatch.split();

        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());

        long splitTime = stopWatch.getSplitTime();
        assertEquals(splitTime, 0);

        assertEquals("0:00:00.000", stopWatch.toString());

        stopWatch.start();

        stopWatch.split();

        long splitTime2 = stopWatch.getSplitTime();
        assertEquals(splitTime2, splitTime);

        assertEquals("0:00:00.001", stopWatch.toString());

        stopWatch.stop();

        long timeElapsed = stopWatch.getTime();
        assertEquals(timeElapsed, 1);

        assertEquals("0:00:01.000", stopWatch.toString());

        assertEquals("0:00:01.000", stopWatch.getSplitTimeString());

        // Test unsplit
        stopWatch.unsplit();

        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());

        stopWatch.stop();

        long timeElapsed2 = stopWatch.getTime();
        assertEquals(timeElapsed + timeElapsed2, timeElapsed + 1);

        assertEquals("0:00:02.000", stopWatch.toString());

        // Test suspend and resume
        stopWatch.start();

        stopWatch.suspend();

        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSuspended());

        long stopTimeBeforeSuspend = System.currentTimeMillis();
        stopWatch.resume();

        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());

        long stopTimeAfterResume = System.currentTimeMillis();
        long timeBetweenSuspendAndResume = stopTimeAfterResume - stopTimeBeforeSuspend;

        assertEquals(stopWatch.getTime(), stopWatch.getTime() + timeBetweenSuspendAndResume);

        assertEquals("0:00:03.000", stopWatch.toString());

        // Test split and suspend
        stopWatch.start();

        stopWatch.split();

        stopWatch.suspend();

        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        assertTrue(stopWatch.isSuspended());

        long splitTimeBeforeSuspend = System.currentTimeMillis();
        stopWatch.resume();

        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());

        long splitTimeAfterResume = System.currentTimeMillis();
        long timeBetweenSuspendAndResume2 = splitTimeAfterResume - splitTimeBeforeSuspend;

        assertEquals(stopWatch.getSplitTime(), stopWatch.getSplitTime() + timeBetweenSuspendAndResume2);

        assertEquals("0:00:03.001", stopWatch.toString());

        stopWatch.stop();

        long timeElapsed3 = stopWatch.getTime();
        assertEquals(timeElapsed + timeElapsed2 + timeElapsed3, timeElapsed + 2);

        assertEquals("0:00:03.001", stopWatch.toString());

        assertEquals("0:00:03.001", stopWatch.getSplitTimeString());

        // Test split and unsplit multiple times
        stopWatch = new StopWatch();

        stopWatch.start();

        stopWatch.split();

        stopWatch.unsplit();

        stopWatch.split();

        stopWatch.unsplit();

        stopWatch.split();

        assertEquals("0:00:03.000", stopWatch.toString());

        stopWatch.stop();

        long timeElapsed4 = stopWatch.getTime();
        assertEquals(timeElapsed4, 3);

        assertEquals("0:00:03.000", stopWatch.toString());

        // Test split and unsplit with negative values
        stopWatch = new StopWatch();

        stopWatch.start();

        stopWatch.split();

        stopWatch.unsplit();

        stopWatch.split();

        stopWatch.unsplit();

        stopWatch.unsplit();

        assertEquals("0:00:00.000", stopWatch.toString());

        stopWatch.stop();

        long timeElapsed5 = stopWatch.getTime();
        assertEquals(timeElapsed5, 0);

        assertEquals("0:00:00.000", stopWatch.toString());

        // Test split and unsplit with overflow
        stopWatch = new StopWatch();

        stopWatch.start();

        long maxLong = Long.MAX_VALUE;

        for (int i = 0; i < 100000; i++) {
            stopWatch.split();
        }

        assertEquals("9223372036854775807:59:59.999", stopWatch.toString());

        stopWatch.stop();

        long timeElapsed6 = stopWatch.getTime();
        assertEquals(timeElapsed6, maxLong + 1);

        assertEquals("9223372036854775808:00:00.001", stopWatch.toString());
    }

    @Test
    public void testDurationFormatUtils() throws ParseException {
        String input = "P1DPT1HPT25S";
        long expected = 86401001 + 3600000 + 25000; // 1 day, 1 hour, 25 seconds
        long duration = Duration.parseDuration(input).getMilliseconds();
        assertEquals(expected, duration);
    }
}