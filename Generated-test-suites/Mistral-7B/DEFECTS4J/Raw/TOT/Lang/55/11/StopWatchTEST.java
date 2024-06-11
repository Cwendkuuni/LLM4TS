package org.apache.commons.lang.time;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link StopWatch}.
 */
public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    /**
     * Test that the watch starts correctly.
     */
    @Test
    public void testStart() {
        stopWatch.start();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.canStart());

        // Time elapsed should be zero since the watch was just started
        assertEquals(0, stopWatch.getTime());
    }

    /**
     * Test that the watch can't be started twice without resetting it.
     */
    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test that the watch can't be stopped before it's started.
     */
    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        stopWatch.stop();
    }

    /**
     * Test that the watch can split the time correctly.
     */
    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isSplit());
        assertFalse(stopWatch.canSplit());

        // Time elapsed since the start should be the same as the split time
        assertEquals(stopWatch.getSplitTime(), stopWatch.getTime());
    }

    /**
     * Test that the watch can unsplit the time correctly.
     */
    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse(stopWatch.isSplit());
        assertTrue(stopWatch.canSplit());

        // Time elapsed since the start should be the same as it was before the split
        assertEquals(stopWatch.getTimeBeforeSplit(), stopWatch.getTime());
    }

    /**
     * Test that the watch can suspend and resume correctly.
     */
    @Test
    public void testSuspendAndResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.getTime() > stopWatch.getTimeBeforeSuspend());
    }

    /**
     * Test that the watch can't suspend a non-running watch.
     */
    @Test(expected = IllegalStateException.class)
    public void testSuspendNonRunning() {
        stopWatch.suspend();
    }

    /**
     * Test that the watch can't resume a non-suspended watch.
     */
    @Test(expected = IllegalStateException.class)
    public void testResumeNonSuspended() {
        stopWatch.resume();
    }

    /**
     * Test that the watch can reset correctly.
     */
    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.reset();
        assertNull(stopWatch.getStartTime());
        assertNull(stopWatch.getStopTime());
        assertNull(stopWatch.getSplitTime());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());

        // The watch should now be ready to be used again
        stopWatch.start();
    }

    /**
     * Test that the watch throws an exception if split is called twice.
     */
    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    /**
     * Test that the watch throws an exception if unsplit is called without a split.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopWatch.start();
        stopWatch.unsplit();
    }

    /**
     * Test that the watch throws an exception if resume is called without a suspend.
     */
    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopWatch.start();
        stopWatch.resume();
    }

    /**
     * Test that the watch throws an exception if start is called twice without a reset.
     */
    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        stopWatch.start();
        stopWatch.start();
    }
}