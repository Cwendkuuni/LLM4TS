package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.split();
        Thread.sleep(200);
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        assertEquals(300, time);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.suspend();
        Thread.sleep(200);
        stopWatch.resume();
        Thread.sleep(300);
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertEquals(600, time);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.getTime());
    }

    @Test
    public void testStopWatchFormat() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        stopWatch.stop();
        String formattedTime = stopWatch.toString();
        String expectedFormat = "1:00:01.000";
        assertEquals(expectedFormat, formattedTime);

        stopWatch.reset();
        stopWatch.start();
        Thread.sleep(500);
        stopWatch.split();
        Thread.sleep(2000);
        stopWatch.unsplit();
        stopWatch.stop();
        String formattedSplitTime = stopWatch.toSplitString();
        String expectedSplitFormat = "1:00:05.500";
        assertEquals(expectedSplitFormat, formattedSplitTime);

        formattedTime = stopWatch.toString();
        String expectedFormatTotal = "1:01:05.500";
        assertEquals(expectedFormatTotal, formattedTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTimeBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getTime();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getSplitTime();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeAfterSplitButNotStopped() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        stopWatch.getSplitTime();
    }
}