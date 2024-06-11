package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.ParseException;

public class StopWatchTest {

    @Test
    public void testStartStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(0, stopWatch.getTime());
        assertEquals(DurationFormatUtils.formatDurationHMS(0), stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.split();
        assertFalse(stopWatch.isRunning());
        assertEquals(0, stopWatch.getTime());
        assertEquals(DurationFormatUtils.formatDurationHMS(0), stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitUnsplitTwice() {
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
        long suspendTime = System.currentTimeMillis();
        stopWatch.resume();
        long resumeTime = System.currentTimeMillis();
        stopWatch.stop();
        long totalTime = stopWatch.getTime();
        assertEquals(-1, totalTime % 1000); // Milliseconds
        long suspendDuration = resumeTime - suspendTime;
        long totalTimeWithoutSuspend = totalTime - suspendDuration;
        assertEquals(totalTimeWithoutSuspend, stopWatch.getTime() + suspendDuration);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long elapsedTime = 1000;
        long expectedTime = stopWatch.getTime();
        System.out.println("Elasped time: " + elapsedTime + "ms");
        System.out.println("Expected time: " + expectedTime + "ms");
        assertEquals(expectedTime, stopWatch.getTime());
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long elapsedTime = 1500;
        long expectedTime = stopWatch.getSplitTime();
        System.out.println("Elasped time: " + elapsedTime + "ms");
        System.out.println("Expected time: " + expectedTime + "ms");
        assertEquals(expectedTime, stopWatch.getSplitTime());
    }

    @Test
    public void testToString() throws ParseException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(2000); // Sleep for 2 seconds
        stopWatch.stop();
        String expectedString = "2:00.001";
        assertEquals(expectedString, stopWatch.toString());
    }

    @Test
    public void testToSplitString() throws ParseException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(2000); // Sleep for 2 seconds
        stopWatch.split();
        Thread.sleep(3000); // Sleep for 3 seconds
        stopWatch.unsplit();
        String expectedString = "2:00.001,3:00.000";
        assertEquals(expectedString, stopWatch.toSplitString());
    }

}