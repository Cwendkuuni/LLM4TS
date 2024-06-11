package org.apache.commons.lang.time;

import static org.junit.Assert.*;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertEquals(StopWatch.STATE_RUNNING, watch.getRunningState());
    }

    @Test
    public void testStop() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertEquals(StopWatch.STATE_STOPPED, watch.getRunningState());
    }

    @Test
    public void testReset() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, watch.getRunningState());
    }

    @Test
    public void testSplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertEquals(StopWatch.STATE_SPLIT, watch.getSplitState());
    }

    @Test
    public void testUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, watch.getSplitState());
    }

    @Test
    public void testSuspend() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, watch.getRunningState());
    }

    @Test
    public void testResume() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        watch.resume();
        assertEquals(StopWatch.STATE_RUNNING, watch.getRunningState());
    }

    @Test
    public void testGetTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertEquals(watch.getStopTime() - watch.getStartTime(), watch.getTime());
    }

    @Test
    public void testGetSplitTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertEquals(watch.getStopTime() - watch.getStartTime(), watch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStart() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStop() {
        StopWatch watch = new StopWatch();
        watch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalResume() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.resume();
    }

    @Test
    public void testToString() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
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