package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.time.StopWatch;

import java.util.Date;

public class StopWatchTest {

    @org.junit.Before
    public void setup() {
        stopWatch = new StopWatch();
    }

    private StopWatch stopWatch;

    @org.junit.Test
    public void testStartAndStop() {
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertEquals(time, 0);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch.start();
        stopWatch.start();
    }

    @org.junit.Test
    public void testSplitAndUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        assertEquals(time, 0);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @org.junit.Test
    public void testSuspendAndResume() {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        stopWatch.resume();
        long time = stopWatch.getTime();
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertEquals(time, elapsedTime);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        stopWatch.suspend();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspended() {
        stopWatch.resume();
    }

    @org.junit.Test
    public void testToString() {
        stopWatch.start();
        stopWatch.split();
        Date startDate = new Date(stopWatch.getStartTime());
        Date stopDate = new Date(stopWatch.getTime());
        String expected = String.format("%s - %s", startDate.toString(), stopDate.toString());
        String actual = stopWatch.toString();
        assertEquals(expected, actual);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        stopWatch.getSplitTime();
    }

    @org.junit.Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        long expected = 100L; // Replace with an actual value
        assertEquals(splitTime, expected);
    }
}