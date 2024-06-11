package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.ParseException;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(0, stopWatch.getTime());
        assertEquals("0.000:00.000.001", stopWatch.toString());
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
        assertEquals("0.000:00.000.001", stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.stop();
        assertEquals(0, stopWatch.getTime());
        assertEquals("0.000:00.000.001", stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testSplitAndGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertEquals(splitTime, stopWatch.getTime());
        assertEquals("0.000:00.000.001", stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getSplitTime();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
        assertEquals("0.000:00.000.001", stopWatch.toString());
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        assertEquals("0.000:00.000.002", stopWatch.toString());
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String splitString = stopWatch.toSplitString();
        try {
            long splitTime = Long.parseLong(splitString.substring(0, splitString.indexOf(":")));
            assertEquals(splitTime / 1000, stopWatch.getSplitTime() / 1000);
        } catch (NumberFormatException e) {
            fail("Failed to parse number from split string.");
        }
    }
}