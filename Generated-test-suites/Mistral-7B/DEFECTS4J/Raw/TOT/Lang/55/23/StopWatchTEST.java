package org.apache.commons.lang.time;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StopWatchTest {

    @Test
    public void testBasicFunctionality() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long timeElapsed = stopWatch.getTime();
        Assert.assertNotEquals(-1, timeElapsed);
        stopWatch.stop();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        String formattedTime = formatter.format(System.currentTimeMillis() - stopWatch.getStartTime());
        Assert.assertEquals(formattedTime, formatter.format(timeElapsed));
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
        stopWatch.start();
    }

    @Test
    public void testSplitFunctionality() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        long timeElapsedBeforeSplit = stopWatch.getSplitTime();
        Assert.assertNotEquals(-1, timeElapsedBeforeSplit);
        stopWatch.stop();
        SimpleDateFormat formatter = new SimpleDateDateFormat("HH:mm:ss.SSS");
        String formattedTime = formatter.format(System.currentTimeMillis() - stopWatch.getStartTime());
        String formattedTimeBeforeSplit = formatter.format(System.currentTimeMillis() - stopWatch.getStartTime() + timeElapsedBeforeSplit);
        Assert.assertEquals("Expected total time to be the sum of split time and remaining time", formattedTime, formattedTimeBeforeSplit);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testSuspendFunctionality() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.resume();
        long timeElapsed = stopWatch.getTime();
        Assert.assertNotEquals(-1, timeElapsed);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        String formattedTime = formatter.format(System.currentTimeMillis() - startTime);
        Assert.assertEquals(formattedTime, formatter.format(timeElapsed));
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testResetFunctionality() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        long timeElapsedBeforeReset = stopWatch.getSplitTime();
        stopWatch.reset();
        long timeElapsedAfterReset = stopWatch.getTime();
        Assert.assertEquals(-1, timeElapsedBeforeReset);
        Assert.assertEquals(-1, timeElapsedAfterReset);
    }
}