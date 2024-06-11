package org.apache.commons.lang.time;

import org.junit.Test;
import static org.junit.Assert.*;

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
        watch.reset();
        assertTrue(watch.getTime() == 0);
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
        assertTrue(watch.getSplitTime() == 0);
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
    public void testGetTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertTrue(watch.getTime() >= 0);
    }

    @Test
    public void testGetSplitTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertTrue(watch.getSplitTime() >= 0);
    }

    @Test
    public void testToString() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertNotNull(watch.toString());
    }

    @Test
    public void testToSplitString() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertNotNull(watch.toSplitString());
    }

}
