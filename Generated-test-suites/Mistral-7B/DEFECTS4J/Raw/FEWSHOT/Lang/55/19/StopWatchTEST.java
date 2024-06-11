package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.ParseException;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertEquals(0, watch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.unsplit();
        assertFalse(watch.isSplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.split();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch watch = new StopWatch();
        watch.start();
        long startTime = watch.getTime();
        watch.suspend();
        watch.resume();
        long endTime = watch.getTime();
        assertEquals(startTime, watch.getTime() - (endTime - startTime));
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        StopWatch watch = new StopWatch();
        watch.suspend();
    }

    @Test
    public void testGetTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        long time = watch.getTime();
        assertEquals(time, watch.getTime());
    }

    @Test
    public void testGetSplitTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        long splitTime = watch.getSplitTime();
        assertEquals(splitTime, watch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        StopWatch watch = new StopWatch();
        watch.getSplitTime();
    }

    @Test
    public void testToString() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        String toString = watch.toString();
        String toSplitString = watch.toSplitString();
        try {
            long time = Long.parseLong(toString);
            long splitTime = Long.parseLong(toSplitString);
            assertEquals(time + splitTime, watch.getTime());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to parse time strings", e);
        }
    }

    @Test
    public void testReset() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.reset();
        assertEquals(0, watch.getTime());
    }

    @Test
    public void testGetTimeAfterReset() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.reset();
        watch.start();
        watch.stop();
        long time = watch.getTime();
        assertEquals(0, time);
    }

}