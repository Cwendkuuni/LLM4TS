package org.apache.commons.lang.time;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertEquals(0, stopWatch.getTime());
        assertTrue(stopWatch.getTime() >= 0);
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
        try {
            stopWatch.getSplitTime();
            fail("Expected IllegalStateException not thrown");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        long time = stopWatch.getTime();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long newTime = stopWatch.getTime();
        assertEquals(time, newTime);
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String splitTime = stopWatch.toSplitString();
        assertTrue(splitTime.contains(":"));
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= 0);
    }

    @Test
    public void testToString() {
        stopWatch.start();
        String time = stopWatch.toString();
        assertTrue(time.contains(":"));
    }
}