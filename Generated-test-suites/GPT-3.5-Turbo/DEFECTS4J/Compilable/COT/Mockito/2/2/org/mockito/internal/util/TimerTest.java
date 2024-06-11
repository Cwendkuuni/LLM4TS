package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);  // Testing with a duration of 1 second
        timer.start();

        // After starting the timer, it should be counting
        assertTrue(timer.isCounting());
        
        // Wait for 1.5 seconds
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // After 1.5 seconds, the timer should have stopped counting
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(2000); // Testing with a duration of 2 seconds

        // Before starting the timer, it should not be counting
        assertFalse(timer.isCounting());

        timer.start();

        // After starting the timer, it should be counting
        assertTrue(timer.isCounting());

        // Wait for 1 second
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // After 1 second, the timer should still be counting
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingWithZeroDuration() {
        Timer timer = new Timer(0); // Testing with a duration of 0 seconds

        timer.start();

        // Since the duration is 0, the timer should stop counting immediately
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWithNegativeDuration() {
        Timer timer = new Timer(-1000); // Testing with a duration of -1 second

        timer.start();

        // Since the duration is negative, the timer should stop counting immediately
        assertFalse(timer.isCounting());
    }

}