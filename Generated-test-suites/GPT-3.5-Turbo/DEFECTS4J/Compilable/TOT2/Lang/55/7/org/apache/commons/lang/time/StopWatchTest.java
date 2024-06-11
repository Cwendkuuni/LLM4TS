package org.apache.commons.lang.time;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart_Stop_Time() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();

        long time = stopWatch.getTime();

        assertTrue(time >= 1000 && time < 1100); // Check if time is approximately 1000ms
    }

    @Test
    public void testSplit_Unsplit_Time() {
        stopWatch.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.unsplit();

        long time = stopWatch.getTime();

        assertTrue(time >= 1000 && time < 1100); // Check if total time is approximately 1000ms
    }

    @Test
    public void testSuspend_Resume_Time() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.suspend();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.resume();

        long time = stopWatch.getTime();

        assertTrue(time >= 1000 && time < 1100); // Check if total time is approximately 1000ms
    }

    @Test
    public void testReset() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.reset();

        long time = stopWatch.getTime();

        assertEquals(0, time); // Check if time is 0 after reset
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopWatch.resume();
    }

}