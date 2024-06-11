package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertTrue(watch.getTime() >= 0);
    }

    @Test
    public void testStop() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertTrue(watch.getTime() >= 0);
    }

    @Test
    public void testReset() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.reset();
        assertEquals(0, watch.getTime());
    }

    @Test
    public void testSplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertTrue(watch.getSplitTime() >= 0);
    }

    @Test
    public void testUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.unsplit();
        assertEquals(0, watch.getSplitTime());
    }

    @Test
    public void testSuspend() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        assertTrue(watch.getTime() >= 0);
    }

    @Test
    public void testResume() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        watch.resume();
        assertTrue(watch.getTime() >= 0);
    }

    @Test
    public void testToString() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertTrue(watch.toString().matches("\\d{2}:\\d{2}:\\d{2}.\\d{3}"));
    }

    @Test
    public void testToSplitString() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertTrue(watch.toSplitString().matches("\\d{2}:\\d{2}:\\d{2}.\\d{3}"));
    }
}