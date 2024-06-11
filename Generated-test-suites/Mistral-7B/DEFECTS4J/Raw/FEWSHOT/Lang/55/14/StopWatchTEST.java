package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.ParseException;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.stop();
        assertEquals(0, sw.getTime());
        assertFalse(sw.isRunning());
        assertNull(sw.getSplitTime());
        assertNull(sw.toString());
        assertNull(sw.toSplitString());
    }

    @Test
    public void testStartAndSplit() throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start();
        Thread.sleep(1000);
        long splitTime = sw.split();
        sw.stop();
        assertEquals(1000, sw.getTime());
        assertFalse(sw.isRunning());
        assertEquals(0, sw.getSplitTime());
        assertEquals("0:0:1.0", sw.toString());
        assertEquals("0:0:1.0", sw.toSplitString());
        assertEquals(DurationFormatUtils.parseDuration("1:0:0.0"), sw.getSplitTimeDuration());
    }

    @Test
    public void testStartSplitAndStop() throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start();
        Thread.sleep(1000);
        long splitTime = sw.split();
        sw.stop();
        sw.start();
        Thread.sleep(1000);
        sw.split();
        sw.stop();
        assertEquals(2000, sw.getTime());
        assertFalse(sw.isRunning());
        assertEquals(1000, sw.getSplitTime());
        assertEquals("0:0:1.0", sw.toString());
        assertEquals("0:0:1.0", sw.toSplitString());
        assertEquals(DurationFormatUtils.parseDuration("1:0:0.0"), sw.getSplitTimeDuration());
        assertEquals("0:0:2.0", sw.toString());
        assertEquals("0:0:2.0", sw.toSplitString());
        assertEquals(DurationFormatUtils.parseDuration("2:0:0.0"), sw.getTimeDuration());
    }

    @Test
    public void testStartSuspendResumeAndStop() throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start();
        Thread.sleep(1000);
        sw.suspend();
        Thread.sleep(1000);
        sw.resume();
        sw.stop();
        assertEquals(2000, sw.getTime());
        assertFalse(sw.isRunning());
        assertNull(sw.getSplitTime());
        assertEquals("0:0:2.0", sw.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.split();
        sw.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendTwice() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.suspend();
        sw.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeTwice() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.suspend();
        sw.resume();
        sw.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        StopWatch sw = new StopWatch();
        sw.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        StopWatch sw = new StopWatch();
        sw.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspended() {
        StopWatch sw = new StopWatch();
        sw.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitAfterStop() {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.stop();
        sw.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch sw = new StopWatch();
        sw.unsplit();
    }

    @Test
    public void testGetSplitTimeDuration() {
        StopWatch sw = new StopWatch();
        sw.start();
        Thread.sleep(1000);
        long splitTime = sw.split();
        sw.stop();
        assertEquals(DurationFormatUtils.parseDuration("1:0:0.0"), sw.getSplitTimeDuration());
    }

    @Test
    public void testToStringAndToSplitString() throws ParseException {
        StopWatch sw = new StopWatch();
        sw.start();
        Thread.sleep(1000);
        long splitTime = sw.split();
        sw.stop();
        assertEquals("1:0:0.0", sw.toString());
        assertEquals("1:0:0.0", sw.toSplitString());
        assertEquals("1:0:0.0", DurationFormatUtils.formatDurationHMS(sw.getSplitTime()));
    }

}