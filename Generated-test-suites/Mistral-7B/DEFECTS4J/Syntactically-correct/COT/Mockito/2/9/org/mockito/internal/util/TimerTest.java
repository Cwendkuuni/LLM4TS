package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsCounting_whenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue("Timer should be counting", timer.isCounting());
    }

    @Test
    public void testTimerIsNotCounting_whenTimerIsNotRunning() {
        Timer timer = new Timer(1000);
        assertFalse("Timer should not be counting", timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testAssertionError_whenTimerIsNotInitialized() {
        Timer timer = new Timer(-1); // invalid duration
        timer.isCounting();
    }

    @Test
    public void testTimerCountDown_whenTimeElapsed() throws InterruptedException {
        Timer timer = new Timer(2000);
        timer.start();
        Thread.sleep(3000); // let the timer count down for some time
        assertTrue("Timer should be not counting", timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException_whenTimerIsAlreadyRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

    @Test
    public void testTimerCountDown_whenTimeIsUp() {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(1500); // let the timer almost finish counting
        assertFalse("Timer should not be counting", timer.isCounting());
    }

    @Test
    public void testTimerCountDown_whenTimerIsStopped() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.stop(); // simulate timer being stopped
        assertFalse("Timer should not be counting", timer.isCounting());
    }

    @Test
    public void testTimerReset_whenTimerIsStopped() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.stop();
        timer.start(); // reset the timer
        assertTrue("Timer should be counting", timer.isCounting());
    }

}