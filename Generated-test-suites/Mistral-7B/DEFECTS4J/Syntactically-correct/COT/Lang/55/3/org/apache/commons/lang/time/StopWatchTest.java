package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

/**
 * Test class for StopWatch.
 *
 * @author John Doe
 */
public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertFalse(stopWatch.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertNull(stopWatch.getTime());
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isSplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse(stopWatch.isSplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.isSuspended());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertFalse(stopWatch.isSuspended());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(100);
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTimeBeforeStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getTime();
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(100);
        stopWatch.split();
        TimeUnit.MILLISECONDS.sleep(100);
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(100);
        stopWatch.stop();
        String timeString = stopWatch.toString();
        assertNotEquals("", timeString);
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(100);
        stopWatch.split();
        TimeUnit.MILLISECONDS.sleep(100);
        String splitTimeString = stopWatch.toSplitString();
        assertNotEquals("", splitTimeString);
    }

}