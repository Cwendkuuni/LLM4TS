package org.apache.commons.lang.time;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setup() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.getTime() >= 0);
        assertEquals(0, stopWatch.getSplitTime());
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
        // Add some delay to simulate the stopwatch running
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        assertTrue(stopWatch.getTime() > 0);
        assertEquals(stopWatch.getTime(), stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testReset() {
        stopWatch.start();
        stopWatch.reset();
        stopWatch.stop();
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        // Add some delay to simulate the stopwatch running
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() > 0);
        // Resume the stopwatch and compare the new split time
        stopWatch.resume();
        // Add some delay to simulate the stopwatch running
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() > stopWatch.getSplitTime());
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
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendTwice() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
    }

    @Test
    public void testSuspendAndResume() {
        stopWatch.start();
        // Add some delay to simulate the stopwatch running
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.suspend();
        long elapsedTimeBeforeResume = stopWatch.getTime();
        // Add some delay to simulate the stopwatch suspended
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.resume();
        long elapsedTimeAfterResume = stopWatch.getTime();
        assertTrue(elapsedTimeAfterResume > elapsedTimeBeforeResume);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        // Add some delay to simulate the stopwatch running
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long elapsedTime = stopWatch.getTime();
        // Stop the stopwatch and compare the elapsed time
        stopWatch.stop();
        assertEquals(elapsedTime, stopWatch.getTime());
    }

    @Test
    public void testToString() {
        stopWatch.start();
        // Add some delay to simulate the stopwatch running
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        // Add some delay to simulate the stopwatch running
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        // Add some delay to simulate the stopwatch running
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String splitTimeString = stopWatch.toSplitString();
        assertNotNull(splitTimeString);
    }
}