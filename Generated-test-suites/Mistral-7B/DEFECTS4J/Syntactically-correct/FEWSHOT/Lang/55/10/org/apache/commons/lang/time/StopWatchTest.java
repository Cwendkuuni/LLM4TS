package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class StopWatchTest {
    @Test
    public void testStopWatchStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testStopWatchStartSplitAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        assertEquals(0, stopWatch.getTime());
        assertEquals(0, stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testStopWatchSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.stop();
        assertEquals(0, stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test
    public void testStopWatchSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long suspensionStart = System.currentTimeMillis();
        stopWatch.suspend();
        long suspensionEnd = System.currentTimeMillis();
        stopWatch.resume();
        long resumedEnd = System.currentTimeMillis();

        long expectedSuspensionDuration = suspensionEnd - suspensionStart;
        long elapsedTime = stopWatch.getTime();
        long totalTime = elapsedTime + expectedSuspensionDuration;

        assertEquals(totalTime, stopWatch.getTime());
        assertEquals(elapsedTime, stopWatch.getTime() - expectedSuspensionDuration);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchSuspendWithoutRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchResumeWithoutSuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testStopWatchToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();

        String expectedToString = "0s";
        String expectedToSplitString = "0s";

        String toString = stopWatch.toString();
        String toSplitString = stopWatch.toSplitString();

        assertEquals(expectedToString, toString);
        assertEquals(expectedToSplitString, toSplitString);
    }

    @Test
    public void testStopWatchGetTimeBetweenStartAndSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();

        long expectedTime = TimeUnit.MILLISECONDS.toMillis(100);
        long time = stopWatch.getTime();

        assertTrue(time >= expectedTime);
    }

    @Test
    public void testStopWatchGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();

        long expectedSplitTime = TimeUnit.MILLISECONDS.toMillis(200);
        long splitTime = stopWatch.getSplitTime();

        assertTrue(splitTime >= expectedSplitTime);
    }
}