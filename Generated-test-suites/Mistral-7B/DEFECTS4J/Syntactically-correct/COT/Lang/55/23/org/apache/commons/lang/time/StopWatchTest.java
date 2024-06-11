package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Test class for StopWatch class.
 */
public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    // Test case for start method
    @Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
    }

    // Test case for stop method
    @Test
    public void testStop() {
        stopWatch.start();
        long startTime = stopWatch.getTime();
        stopWatch.stop();
        long endTime = stopWatch.getTime();
        long elapsedTime = endTime - startTime;
        assertEquals(elapsedTime, 0); // Test for zero elapsed time in the first test case

        stopWatch.start();
        // Perform some operation here to let the time elapse
        stopWatch.stop();
        elapsedTime = endTime - startTime;
        assert(elapsedTime > 0);
    }

    // Test case for reset method
    @Test
    public void testReset() {
        stopWatch.start();
        long startTime = stopWatch.getTime();
        stopWatch.reset();
        long endTime = stopWatch.getTime();
        long elapsedTime = endTime - startTime;
        assertEquals(elapsedTime, 0);
    }

    // Test case for split method
    @Test
    public void testSplit() {
        stopWatch.start();
        long startTime = stopWatch.getTime();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        long elapsedTime = splitTime - startTime;
        assert(elapsedTime > 0);

        stopWatch.unsplit();
        elapsedTime = stopWatch.getTime() - startTime;
        assertEquals(elapsedTime, splitTime);
    }

    // Test case for unsplit method
    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplit() {
        stopWatch.unsplit();
    }

    // Test case for suspend method
    @Test
    public void testSuspend() {
        stopWatch.start();
        long startTime = stopWatch.getTime();
        stopWatch.suspend();
        long suspendTime = System.currentTimeMillis();
        stopWatch.resume();
        long endTime = stopWatch.getTime();
        long elapsedTime = endTime - startTime;
        long suspendedTime = suspendTime - startTime;
        long totalElapsedTime = elapsedTime + suspendedTime;
        assertEquals(totalElapsedTime, stopWatch.getTime());
    }

    // Test case for resume method
    @Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        stopWatch.resume();
    }

    // Test case for toString method
    @Test
    public void testToString() {
        stopWatch.start();
        // Let the time elapse for some period
        stopWatch.stop();
        String expectedTime = "00:00:01.001";
        String actualTime = stopWatch.toString();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date;
        try {
            date = format.parse(expectedTime);
            long expectedMillis = date.getTime();
            long actualMillis = Long.parseLong(stopWatch.getTime() * 1000L % 1000L + "");
            assertEquals(expectedMillis, actualMillis);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e);
        }
    }

    // Test case for toSplitString method
    @Test
    public void testToSplitString() {
        stopWatch.start();
        // Let the time elapse for some period and split it
        stopWatch.split();
        String expectedSplitTime = "00:00:01.001";
        String actualSplitTime = stopWatch.toSplitString();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date;
        try {
            date = format.parse(expectedSplitTime);
            long expectedMillis = date.getTime();
            long actualMillis = Long.parseLong(stopWatch.getSplitTime() * 1000L % 1000L + "");
            assertEquals(expectedMillis, actualMillis);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e);
        }
    }
}