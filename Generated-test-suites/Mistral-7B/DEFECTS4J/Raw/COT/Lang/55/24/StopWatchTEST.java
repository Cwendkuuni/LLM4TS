package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;

/**
 * Test class for StopWatch.
 */
public class StopWatchTest {

    @org.junit.Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    private StopWatch stopWatch;

    /**
     * Test method for {@link StopWatch#start()}.
     */
    @org.junit.Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
    }

    /**
     * Test method for {@link StopWatch#stop()}.
     */
    @org.junit.Test
    public void testStop() {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.stop();
        long time = stopWatch.getTime();
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertEquals(time, elapsedTime);
    }

    /**
     * Test method for {@link StopWatch#split()}.
     */
    @org.junit.Test
    public void testSplit() {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertEquals(splitTime, elapsedTime);
    }

    /**
     * Test method for {@link StopWatch#unsplit()}.
     */
    @org.junit.Test
    public void testUnsplit() {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertEquals(time, elapsedTime);
    }

    /**
     * Test method for {@link StopWatch#suspend()}.
     */
    @org.junit.Test
    public void testSuspend() {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - startTime;
        stopWatch.resume();
        long time = stopWatch.getTime();
        long elapsedTime = System.currentTimeMillis() - startTime - pauseTime;
        assertEquals(time, elapsedTime);
    }

    /**
     * Test method for {@link StopWatch#resume()}.
     */
    @org.junit.Test
    public void testResume() {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - startTime;
        stopWatch.resume();
        stopWatch.resume();
        long time = stopWatch.getTime();
        long elapsedTime = System.currentTimeMillis() - startTime - pauseTime * 2;
        assertEquals(time, elapsedTime);
    }

    /**
     * Test method for {@link StopWatch#getTime()}.
     */
    @org.junit.Test
    public void testGetTime() {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        stopWatch.stop();
        long time = stopWatch.getTime();
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertEquals(time, elapsedTime);
    }

    /**
     * Test method for {@link StopWatch#getSplitTime()}.
     * @throws ParseException
     */
    @org.junit.Test
    public void testGetSplitTime() throws ParseException {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        stopWatch.stop();
        long splitTime = stopWatch.getSplitTime();
        String expected = "00:00:00.%d";
        long milliseconds = (long) (splitTime % 1000);
        String actual = String.format(expected, milliseconds);
        assertEquals(actual, stopWatch.toSplitString());
    }

    /**
     * Test method for {@link StopWatch#toString()}.
     * @throws ParseException
     */
    @org.junit.Test
    public void testToString() throws ParseException {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        stopWatch.stop();
        String expected = "00:00:00.%d";
        long milliseconds = (long) (stopWatch.getTime() % 1000);
        String actual = stopWatch.toString();
        String actualFormatted = String.format(expected, milliseconds);
        assertEquals(actualFormatted, actual);
    }

    /**
     * Test method for {@link StopWatch#isRunning()}.
     */
    @org.junit.Test
    public void testIsRunning() {
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        stopWatch.stop();
        assertFalse(stopWatch.isRunning());
    }

    /**
     * Test method for {@link StopWatch#reset()}.
     */
    @org.junit.Test
    public void testReset() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        assertTrue(stopWatch.isRunning());
        stopWatch.start();
    }

    /**
     * Test method for {@link StopWatch#getTime()}, {@link StopWatch#split()},
     * {@link StopWatch#getSplitTime()}, and {@link StopWatch#unsplit()}.
     * @throws InterruptedException
     * @throws ParseException
     */
    @org.junit.Test
    public void testSplitAndUnsplit() throws InterruptedException, ParseException {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        Thread.sleep(100);
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertEquals(time, elapsedTime);
    }

    /**
     * Test method for {@link StopWatch#getTime()}, {@link StopWatch#split()},
     * {@link StopWatch#getSplitTime()}, and {@link StopWatch#suspend()}.
     * @throws InterruptedException
     * @throws ParseException
     */
    @org.junit.Test
    public void testSuspendSplitAndResume() throws InterruptedException, ParseException {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        Thread.sleep(100);
        stopWatch.suspend();
        Thread.sleep(1000);
        stopWatch.resume();
        long time = stopWatch.getTime();
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertEquals(time, elapsedTime);
    }

    /**
     * Test method for {@link StopWatch#getTime()}, {@link StopWatch#split()},
     * {@link StopWatch#getSplitTime()}, and {@link StopWatch#resume()}.
     * @throws InterruptedException
     * @throws ParseException
     */
    @org.junit.Test
    public void testResumeSplitAndResume() throws InterruptedException, ParseException {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        Thread.sleep(100);
        stopWatch.resume();
        stopWatch.resume();
        Thread.sleep(1000);
        stopWatch.resume();
        long time = stopWatch.getTime();
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertEquals(time, elapsedTime);
    }

    /**
     * Test method for {@link StopWatch#getTime()}, {@link StopWatch#reset()},
     * {@link StopWatch#start()}, and {@link StopWatch#split()}.
     * @throws InterruptedException
     * @throws ParseException
     */
    @org.junit.Test
    public void testResetStartSplit() throws InterruptedException, ParseException {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        stopWatch.reset();
        assertTrue(stopWatch.isRunning());
        stopWatch.start();
    }

    /**
     * Test method for {@link StopWatch#getTime()}, {@link StopWatch#reset()},
     * {@link StopWatch#start()}, and {@link StopWatch#split()}, {@link StopWatch#split()}.
     * @throws InterruptedException
     * @throws ParseException
     */
    @org.junit.Test
    public void testResetStartSplitSplit() throws InterruptedException, ParseException {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        stopWatch.split();
        stopWatch.reset();
        assertTrue(stopWatch.isRunning());
        stopWatch.start();
    }

    /**
     * Test method for {@link StopWatch#toString()}, {@link StopWatch#getSplitTime()},
     * and {@link StopWatch#split()}.
     * @throws InterruptedException
     * @throws ParseException
     */
    @org.junit.Test
    public void testToStringSplit() throws InterruptedException, ParseException {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        Thread.sleep(100);
        stopWatch.stop();
        String expected = "00:00:00.%d";
        long milliseconds = (long) (stopWatch.getSplitTime() % 1000);
        String actual = stopWatch.toString();
        String actualFormatted = String.format(expected, milliseconds);
        assertEquals(actualFormatted, actual);
    }
}