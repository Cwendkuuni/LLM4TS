package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.Date;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        assertTrue(stopWatch.isStopped());
        assertTrue(stopWatch.isSplit());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.stop();
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        assertEquals(-1, time);
        stopWatch.split();
        time = stopWatch.getTime();
        assertTrue(time > 0);
        stopWatch.stop();
        time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertEquals(-1, splitTime);
        stopWatch.stop();
        splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime > 0);
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String timeString = stopWatch.toString();
        String expected = "0s";
        assertEquals(expected, timeString);
        stopWatch.stop();
        timeString = stopWatch.toString();
        String expectedTime = "0s";
        String expectedSplit = "0s";
        long time = System.currentTimeMillis();
        Date start = new Date(time - 1000);
        Date stop = new Date(time);
        String expectedTimeFormat = String.format("%d:%02d:%02d.%03d", 0, 0, 0, 0);
        String expectedSplitFormat = String.format("%d:%02d:%02d.%03d", 0, 0, 0, 0);
        assertEquals(expectedTimeFormat, expectedTimeFormat.equals(expectedTimeString(expectedTime)) ? expectedTimeString(expectedTimeFormat) : DurationFormatUtils.formatDurationHMS(0));
        assertEquals(expectedSplitFormat, expectedSplitFormat.equals(expectedSplitString(expectedSplit)) ? expectedSplitString(expectedSplitFormat) : DurationFormatUtils.formatDurationHMS(0));
        assertEquals(expectedTime, timeString.substring(0, expectedTime.length() + 1));
        assertEquals(expectedSplit, timeString.substring(expectedTime.length() + 1));
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String splitString = stopWatch.toSplitString();
        String expected = "0s";
        assertEquals(expected, splitString);
        stopWatch.stop();
        splitString = stopWatch.toSplitString();
        long time = System.currentTimeMillis();
        Date start = new Date(time - 1000);
        Date stop = new Date(time);
        String expectedTimeFormat = String.format("%d:%02d:%02d.%03d", 0, 0, 0, 0);
        String expectedSplitFormat = String.format("%d:%02d:%02d.%03d", 0, 0, 0, 0);
        assertEquals(expectedSplitFormat, expectedSplitFormat.equals(expectedSplitString(expectedSplitFormat)) ? expectedSplitString(expectedSplitFormat) : DurationFormatUtils.formatDurationHMS(0));
        assertEquals(expectedTime, splitString.substring(0, expectedTime.length() + 1));
        assertEquals(expectedSplit, splitString.substring(expectedTime.length() + 1));
    }

    private String expectedTimeString(String expectedTime) {
        return DurationFormatUtils.formatDurationHMS(Long.parseLong(expectedTime.replaceAll("[^0-9.]", "")) * 1000);
    }

    private String expectedSplitString(String expectedSplit) {
        return DurationFormatUtils.formatDurationHMS(Long.parseLong(expectedSplit.replaceAll("[^0-9.]", "")) * 1000);
    }

}