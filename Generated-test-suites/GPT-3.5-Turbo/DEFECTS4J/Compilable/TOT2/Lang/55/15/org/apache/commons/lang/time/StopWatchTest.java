package org.apache.commons.lang.time;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertEquals(0, stopWatch.getSplitTime());
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        try {
            stopWatch.getSplitTime();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testSuspendAndResume() {
        stopWatch.start();
        stopWatch.suspend();
        long suspendedTime = stopWatch.getTime();
        stopWatch.resume();
        assertTrue(stopWatch.getTime() >= suspendedTime);
    }

    @Test
    public void testToString() {
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
    }

    @Test
    public void testToSplitString() {
        try {
            stopWatch.toSplitString();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testIllegalState() {
        try {
            stopWatch.stop();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

}