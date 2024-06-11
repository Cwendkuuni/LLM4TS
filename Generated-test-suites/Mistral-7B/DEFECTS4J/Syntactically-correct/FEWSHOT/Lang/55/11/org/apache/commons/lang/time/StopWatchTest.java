package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartAndStop() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        long actual = watch.getTime();
        long expected = 0; // the test should not take any time
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.start();
    }

    @Test
    public void testStopBeforeStart() throws Exception {
        StopWatch watch = new StopWatch();
        watch.stop();
        watch.start();
        long actual = watch.getTime();
        long expected = 0; // the test should not take any time
        assertEquals(expected, actual);
    }

    @Test
    public void testSplit() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(10L);
        watch.split();
        long actual = watch.getTime();
        long expected = 10;
        assertEquals(expected, actual);
        long actualSplit = watch.getSplitTime();
        assertEquals(expected, actualSplit);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() throws Exception {
        StopWatch watch = new StopWatch();
        watch.split();
    }

    @Test
    public void testUnsplit() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(10L);
        watch.split();
        watch.unsplit();
        long actual = watch.getTime();
        long expected = 10;
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitBeforeSplit() throws Exception {
        StopWatch watch = new StopWatch();
        watch.unsplit();
    }

    @Test
    public void testSuspendAndResume() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(100);
        watch.suspend();
        Thread.sleep(50);
        watch.resume();
        Thread.sleep(50);
        watch.stop();
        long actual = watch.getTime();
        long expected = 150;
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() throws Exception {
        StopWatch watch = new StopWatch();
        watch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() throws Exception {
        StopWatch watch = new StopWatch();
        watch.resume();
    }

    @Test
    public void testToString() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(1000);
        watch.stop();
        String actual = watch.toString();
        String expected = "1:00.005";
        assertEquals(expected, actual);
    }

    @Test
    public void testToSplitString() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(1000);
        watch.split();
        watch.stop();
        String actual = watch.toSplitString();
        String expected = "1:00.005";
        assertEquals(expected, actual);
    }
}