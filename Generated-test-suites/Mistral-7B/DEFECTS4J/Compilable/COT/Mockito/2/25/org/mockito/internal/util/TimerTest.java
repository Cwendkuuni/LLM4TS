package org.mockito.internal.util; // make sure the test package matches the class package

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsCounting_whenTimerIsRunning() {
        Timer timer = new Timer(1000L);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsCounting_whenTimerHasNotBeenStarted() {
        Timer timer = new Timer(1000L);
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testTimerIsCounting_whenStartTimeIsNotFound() {
        Timer timer = new Timer(1000L);
        timer.isCounting(); // the test will fail due to the AssertionError
    }

    @Test
    public void testTimerStart_whenTimerIsNotRunning() {
        Timer timer = new Timer(1000L);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testTimerStart_whenTimerIsAlreadyRunning() {
        Timer timer = new Timer(1000L);
        timer.start();
        timer.start(); // the test will fail due to the IllegalStateException
    }

    @Test
    public void testTimerStart_whenDurationIsZero() {
        Timer timer = new Timer(0L);
        timer.start();
        assertTrue(timer.isCounting()); // the timer should not throw an exception with a zero duration
        assertTrue(timer.isCounting()); // the timer should not finish counting down immediately
    }

    @Test
    public void testTimerStart_whenDurationIsNegative() {
        Timer timer = new Timer(-1L);
        timer.start();
        assertFalse(timer.isCounting()); // the timer should not start counting down with a negative duration
    }

}