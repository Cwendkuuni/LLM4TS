package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.Duration;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

/**
 * Test class for {@link StopWatch}.
 */
public class StopWatchTest {

    @Test
    public void testConstructor() {
        StopWatch stopWatch = new StopWatch();
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals("", stopWatch.toString());
        assertEquals(0, stopWatch.getTime());
        assertEquals(0, stopWatch.getSplitTime());
    }

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertNotNull(time);
        assertEquals(0, time % 1000);

        stopWatch.reset();
        stopWatch.start();
        stopWatch.stop();
        time = stopWatch.getTime();
        assertNotNull(time);
        assertFalse(time == 0);
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertNotNull(splitTime);
        assertFalse(splitTime == 0);

        stopWatch.unsplit();
        long time = stopWatch.getTime();
        assertNotNull(time);
        assertEquals(splitTime, time);
    }

    @Test
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
        assertThrows(IllegalStateException.class, stopWatch::getSplitTime);
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - startTime;
        stopWatch.resume();
        long endTime = System.currentTimeMillis();
        long time = stopWatch.getTime();
        assertNotNull(time);
        assertEquals(pauseTime, time % (2 * 1000));

        stopWatch.reset();
        stopWatch.start();
        stopWatch.suspend();
        startTime = System.currentTimeMillis();
        stopWatch.resume();
        endTime = System.currentTimeMillis();
        time = stopWatch.getTime();
        assertEquals(0, time % 1000);
    }

    @Test
    public void testSuspendTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
        assertThrows(IllegalStateException.class, stopWatch::resume);
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String result = stopWatch.toString();
        String expected = String.format("00:%02d:%02d.%03d", 0, 0, stopWatch.getTime() % 1000);
        assertEquals(expected, result);

        stopWatch.reset();
        stopWatch.start();
        stopWatch.stop();
        result = stopWatch.toString();
        expected = String.format("00:00:00.%03d", stopWatch.getTime() % 1000);
        assertEquals(expected, result);
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String result = stopWatch.toSplitString();
        String expected = String.format("00:%02d:%02d.%03d", 0, 0, stopWatch.getSplitTime() % 1000);
        assertEquals(expected, result);

        stopWatch.reset();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        result = stopWatch.toSplitString();
        expected = String.format("00:00:00.%03d", stopWatch.getSplitTime() % 1000);
        assertEquals(expected, result);
    }

    @Test
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
        assertThrows(IllegalStateException.class, stopWatch::getTime);
    }

    @Test
    public void testResetAndStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertNotNull(time);
        assertFalse(time == 0);

        stopWatch.reset();
        stopWatch.start();
        stopWatch.stop();
        time = stopWatch.getTime();
        assertNotNull(time);
        assertEquals(0, time % 1000);
    }

}