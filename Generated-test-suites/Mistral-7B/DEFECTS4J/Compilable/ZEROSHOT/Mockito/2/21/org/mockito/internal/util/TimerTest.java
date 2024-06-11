package org.junit.test; // Replace with your package name if necessary

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.util.Timer;

public class TimerTest {

    @Test
    public void testTimerIsCounting() {
        Timer timer = new Timer(1000); // Set duration to 1 second
        timer.start();

        // Test isCounting() while timer is counting
        assertTrue(timer.isCounting());

        // Simulate time passing by sleeping for a short period
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test isCounting() after some time has passed
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCounting() {
        Timer timer = new Timer(1000); // Set duration to 1 second

        // Test isCounting() before starting the timer
        assertFalse(timer.isCounting());

        // Start the timer and wait for it to finish counting
        timer.start();
        try {
            Thread.sleep(1500); // Wait for 1.5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test isCounting() after the timer has finished counting
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testAssertStartedBeforeIsCounting() {
        Timer timer = new Timer(1000);
        timer.isCounting();
        timer.start();
    }

    @Test
    public void testTimerStartTwice() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();

        // Test isCounting() after starting the timer twice
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerWithZeroDuration() {
        Timer timer = new Timer(0);
        timer.start();

        // Test isCounting() after starting the timer with zero duration
        assertFalse(timer.isCounting());
    }

}