package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertStateEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.stop();
        assertEquals(0, sw.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        StopWatch sw = new StopWatch();
        sw.stop();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.split();
        sw.unsplit();
        sw.stop();
        assertEquals(0, sw.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        StopWatch sw = new StopWatch();
        sw.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitBeforeSplit() {
        StopWatch sw = new StopWatch();
        sw.unsplit();
    }

    @Test
    public void testSplitAndGetSplitTime() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.split();
        sw.stop();
        long splitTime = sw.getSplitTime();
        assertEquals(0, sw.getTime());
        assertNotEquals(0, splitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeBeforeSplit() {
        StopWatch sw = new StopWatch();
        sw.getSplitTime();
    }

    @Test
    public void testSuspendAndResume() throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.suspend();
        Thread.sleep(100);
        sw.resume();
        sw.stop();
        long totalTime = sw.getTime();
        assertEquals(100, totalTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        StopWatch sw = new StopWatch();
        sw.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        StopWatch sw = new StopWatch();
        sw.resume();
    }

    @Test
    public void testToString() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.split();
        sw.stop();
        assertEquals("0s", sw.toString());
        sw.reset();
        sw.start();
        sw.split();
        sw.split();
        sw.stop();
        String result = sw.toString();
        SimpleDateFormat formatter = new SimpleDateFormat("0s");
        Date date = formatter.parse("0s");
        long expected = Date.parse(result) - date.getTime();
        assertEquals(2 * 1000L, expected);
    }

    @Test
    public void testToSplitString() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.split();
        sw.stop();
        assertEquals("0s", sw.toSplitString());
        sw.reset();
        sw.start();
        sw.split();
        sw.split();
        sw.stop();
        String result = sw.toSplitString();
        SimpleDateFormat formatter = new SimpleDateFormat("0s");
        Date date = formatter.parse("0s");
        long expected = Date.parse(result) - date.getTime();
        assertEquals(2 * 1000L, expected);
    }

    @Test
    public void testGetTime() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.split();
        sw.split();
        sw.stop();
        long totalTime = sw.getTime();
        assertEquals(3 * 1000L, totalTime);
    }

    @Test
    public void testGetState() {
        StopWatch sw = new StopWatch();
        assertStateEquals(StopWatch.STATE_UNSTARTED, sw);
        sw.start();
        assertStateEquals(StopWatch.STATE_RUNNING, sw);
        sw.split();
        assertStateEquals(StopWatch.STATE_SPLIT, sw);
        sw.stop();
        assertStateEquals(StopWatch.STATE_STOPPED, sw);
        sw.reset();
        assertStateEquals(StopWatch.STATE_UNSTARTED, sw);
    }

    @Test
    public void testSleepAndGetTime() throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start();
        TimeUnit.MILLISECONDS.sleep(10);
        sw.stop();
        long totalTime = sw.getTime();
        assertEquals(10L, totalTime);
    }
}