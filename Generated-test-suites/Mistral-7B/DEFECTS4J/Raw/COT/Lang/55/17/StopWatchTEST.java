package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;

public class StopWatchTest {

    @org.junit.Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @org.junit.Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertFalse(stopWatch.isRunning());
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertEquals(System.currentTimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
    }

    @org.junit.Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        assertTrue(stopWatch.isReset());
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @org.junit.Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @org.junit.Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.isSuspended());
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @org.junit.Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertFalse(stopWatch.isSuspended());
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(System.currentTimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @org.junit.Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        stopWatch.stop();
        assertEquals(time, stopWatch.getTime());
    }

    @org.junit.Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        assertEquals(splitTime, stopWatch.getSplitTime());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        long splitTime = stopWatch.getSplitTime();
    }

    @org.junit.Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String toString = stopWatch.toString();
        stopWatch.stop();
        assertNotNull(toString);
    }

    @org.junit.Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String toSplitString = stopWatch.toSplitString();
        stopWatch.unsplit();
        assertNotNull(toSplitString);
    }

    @org.junit.Test
    public void testGetRunningState() {
        StopWatch stopWatch = new StopWatch();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getRunningState());
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    @org.junit.Test
    public void testIsRunning() {
        StopWatch stopWatch = new StopWatch();
        assertFalse(stopWatch.isRunning());
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        stopWatch.split();
        assertTrue(stopWatch.isRunning());
        stopWatch.unsplit();
        assertTrue(stopWatch.isRunning());
        stopWatch.stop();
        assertFalse(stopWatch.isRunning());
    }

    @org.junit.Test
    public void testIsSuspended() {
        StopWatch stopWatch = new StopWatch();
        assertFalse(stopWatch.isSuspended());
        stopWatch.start();
        assertFalse(stopWatch.isSuspended());
        stopWatch.suspend();
        assertTrue(stopWatch.isSuspended());
        stopWatch.resume();
        assertFalse(stopWatch.isSuspended());
    }

    @org.junit.Test
    public void testIsReset() {
        StopWatch stopWatch = new StopWatch();
        assertFalse(stopWatch.isReset());
        stopWatch.reset();
        assertTrue(stopWatch.isReset());
    }

    @org.junit.Test
    public void testParseDurationHMS() throws ParseException {
        long duration = DurationFormatUtils.parseDuration("PT1S");
        assertEquals(1000, duration);
        duration = DurationFormatUtils.parseDuration("PT1M30S");
        assertEquals(60000 + 30000, duration);
        duration = DurationFormatUtils.parseDuration("PT1H2M30S");
        assertEquals(3600000 + 60000 + 30000, duration);
    }
}