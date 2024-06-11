package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testUnstartedStopWatch() {
        StopWatch stopWatch = new StopWatch();
        assertEquals(0, stopWatch.getTime());
        assertEquals(0, stopWatch.getSplitTime());
        assertEquals("0.000:00.000.001", stopWatch.toString());
        assertEquals("0.000:00.000.001", stopWatch.toSplitString());
        assertNull(stopWatch.startTime);
        assertNull(stopWatch.stopTime);
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.hasSplit());
        assertFalse(stopWatch.isSuspended());
    }

    @Test
    public void testRunningStopWatch() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertEquals(0, stopWatch.getTime());
        assertNull(stopWatch.stopTime);

        long pauseTime = System.currentTimeMillis();
        stopWatch.suspend();
        assertTrue(stopWatch.isSuspended());
        long elapsed = System.currentTimeMillis() - pauseTime;
        assertEquals(-elapsed, stopWatch.getTime());

        pauseTime = System.currentTimeMillis();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertEquals(-(pauseTime - stopWatch.startTime), stopWatch.getTime());
    }

    @Test
    public void testSplitStopWatch() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long pauseTime = System.currentTimeMillis();
        stopWatch.split();
        assertTrue(stopWatch.hasSplit());
        assertEquals(-(pauseTime - stopWatch.startTime), stopWatch.getSplitTime());
        assertEquals(stopWatch.getTime(), stopWatch.getSplitTime());
        assertEquals(stopWatch.getSplitTime(), stopWatch.getTime());
        assertNull(stopWatch.stopTime);

        pauseTime = System.currentTimeMillis();
        stopWatch.stop();
        assertTrue(stopWatch.isStopped());
        assertEquals(stopWatch.getSplitTime(), stopWatch.getTime());
        assertEquals(stopWatch.getSplitTime(), stopWatch.getTime());

        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
        assertEquals(0, stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalSplitAfterStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalSuspendAfterSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        stopWatch.start();
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long pauseTime = System.currentTimeMillis();
        stopWatch.split();
        stopWatch.stop();
        assertEquals("0.001:00.000.002", stopWatch.toString());
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long pauseTime = System.currentTimeMillis();
        stopWatch.split();
        stopWatch.stop();
        assertEquals("0.001:00.000.002", stopWatch.toSplitString());
    }
}