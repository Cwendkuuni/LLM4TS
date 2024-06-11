package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.ParseException;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(0, stopWatch.getTime());
        assertFalse(stopWatch.isRunning());
        assertNull(stopWatch.getStartTime());
        assertNull(stopWatch.getStopTime());
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
        assertEquals(0, stopWatch.getTime());
        assertNull(stopWatch.getStartTime());
        assertNull(stopWatch.getStopTime());
        assertFalse(stopWatch.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testSuspendResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.stop();
        assertEquals(0, stopWatch.getTime());
        assertFalse(stopWatch.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long duration = 500;
        long elapsed = stopWatch.getTime();
        stopWatch.stop();
        assertEquals(duration, elapsed);
        assertFalse(stopWatch.isRunning());
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long duration = 500;
        long elapsed = stopWatch.getSplitTime();
        stopWatch.unsplit();
        stopWatch.stop();
        assertEquals(duration, elapsed);
        assertFalse(stopWatch.isRunning());
    }

    @Test
    public void testToString() throws ParseException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1500);
        stopWatch.stop();
        String result = stopWatch.toString();
        long duration = Long.parseLong(DurationFormatUtils.parseDuration(result).getMillis() + "");
        assertEquals(1500, duration);
    }

    @Test
    public void testToSplitString() throws ParseException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        Thread.sleep(1500);
        stopWatch.unsplit();
        stopWatch.stop();
        String result = stopWatch.toSplitString();
        long duration = Long.parseLong(DurationFormatUtils.parseDuration(result).getMillis() + "");
        assertEquals(1500, duration);
    }
}