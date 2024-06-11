package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStopWatchLifeCycle() {
        StopWatch stopWatch = new StopWatch();
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
        stopWatch.start();
        //Start should not return any time, but should set the start time
        assertEquals(0, stopWatch.getTime());
        assertEquals(0, stopWatch.getSplitTime());
        assertNotNull(stopWatch.toString());
        assertNotNull(stopWatch.toSplitString());
        stopWatch.split();
        //Split should return zero time, but set the stop time and change state
        assertEquals(0, stopWatch.getTime());
        assertNotNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNotNull(stopWatch.toSplitString());
        stopWatch.stop();
        //Stop should return the time between start and stop
        assertEquals(1, stopWatch.getTime());
        assertEquals(0, stopWatch.getSplitTime());
        assertNotNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
        //Reset should clear all state and return zero time
        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
        assertEquals(0, stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    @Test
    public void testStopWatchLifeCycleWithSuspendResume() {
        StopWatch stopWatch = new StopWatch();
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.split();
        //Suspend/resume should not affect the time between start and split
        assertEquals(1, stopWatch.getTime());
        assertNotNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNotNull(stopWatch.toSplitString());
        stopWatch.stop();
        assertEquals(1, stopWatch.getTime());
        assertEquals(1, stopWatch.getSplitTime());
        assertNotNull(stopWatch.toString());
        assertNotNull(stopWatch.toSplitString());
        stopWatch.reset();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchIllegalLifeCycle1() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchIllegalLifeCycle2() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchIllegalLifeCycle3() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchIllegalLifeCycle4() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
        stopWatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchIllegalLifeCycle5() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchIllegalLifeCycle6() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.resume();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchIllegalLifeCycle7() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
        stopWatch.resume();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchIllegalLifeCycle8() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.resume();
        stopWatch.stop();
        stopWatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchIllegalLifeCycle9() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.suspend();
        stopWatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWatchIllegalLifeCycle10() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testStopWatchLongRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long duration = 10000; //10 seconds
        long elapsed = 0;
        while (elapsed < duration) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            elapsed += 10;
        }
        stopWatch.stop();
        long totalTime = stopWatch.getTime();
        assertTrue("Total time is less than expected: " + totalTime + " < " + duration, totalTime > duration);
    }
}