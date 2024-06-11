package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {
    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        stopWatch.stop();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= 0);
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse(stopWatch.getSplitTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitTwice() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopWatch.start();
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test
    public void testToString() {
        stopWatch.start();
        assertTrue(stopWatch.toString().matches("\\d{2}:\\d{2}:\\d{2}.\\d{3}"));
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.toSplitString().matches("\\d{2}:\\d{2}:\\d{2}.\\d{3}"));
    }
}