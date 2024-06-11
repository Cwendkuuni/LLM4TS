package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStopWatch() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        assertEquals(1001L, stopWatch.getTime()); // +- some millis
        assertEquals("1s", DurationFormatUtils.formatDuration(1001L));
        assertEquals("1s", stopWatch.toString());

        stopWatch.reset();
        assertNull(stopWatch.getTime());
        assertFalse(stopWatch.isRunning());

        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        assertEquals(1001L, stopWatch.getSplitTime());
        assertEquals("1s", DurationFormatUtils.formatDuration(1001L));
        assertEquals("0s", stopWatch.toString());
        assertEquals("1s", stopWatch.toSplitString());

        stopWatch.reset();
        stopWatch.start();
        stopWatch.suspend();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.resume();
        stopWatch.stop();
        assertEquals(1501L, stopWatch.getTime()); // +- some millis
        assertEquals("1s 500ms", DurationFormatUtils.formatDuration(1501L));
        assertEquals("1s 500ms", stopWatch.toString());

        stopWatch.reset();
        stopWatch.start();
        stopWatch.split();
        stopWatch.suspend();
        stopWatch.split();
        stopWatch.resume();
        stopWatch.stop();
        assertEquals(2001L, stopWatch.getSplitTime()); // +- some millis
        assertEquals("1s 1ms", stopWatch.toString());
        assertEquals("1s", stopWatch.toSplitString());
        assertEquals("1s 1ms", stopWatch.getSplitString());

        stopWatch.reset();
        stopWatch.start();
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        stopWatch.split();
        stopWatch.split();
        stopWatch.stop();
        assertEquals(3001L, stopWatch.getTime()); // +- some millis
        assertEquals("3s", DurationFormatUtils.formatDuration(3001L));
        assertEquals("3s", stopWatch.toString());

        stopWatch.reset();
        stopWatch.start();
        stopWatch.suspend();
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.resume();
        stopWatch.split();
        stopWatch.stop();
        assertEquals(1001L, stopWatch.getTime()); // +- some millis
        assertEquals("1s", DurationFormatUtils.formatDuration(1001L));
        assertEquals("1s", stopWatch.toString());

        stopWatch.reset();
        stopWatch.start();
        stopWatch.split();
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        stopWatch.stop();
        assertEquals(2001L, stopWatch.getTime()); // +- some millis
        assertEquals("2s", DurationFormatUtils.formatDuration(2001L));
        assertEquals("1s", stopWatch.toString());
        assertEquals("1s", stopWatch.getSplitString());
        assertEquals("1s", stopWatch.getSplitString(0));
        assertEquals("1s", stopWatch.toSplitString());
        assertEquals("1s", stopWatch.getSplitString(1));
    }

    @Test
    public void testSplitAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        stopWatch.split();
        assertFalse(stopWatch.isRunning());
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitAfterStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspendTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.resume();
        stopWatch.resume();
    }
}