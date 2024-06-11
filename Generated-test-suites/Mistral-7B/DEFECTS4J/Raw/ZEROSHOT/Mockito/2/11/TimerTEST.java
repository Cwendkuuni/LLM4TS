package org.junit.test; // replace with the actual package where TimerTest.java is located

import org.junit.Test;
import org.mockito.internal.util.Timer;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testTimerIsCounting() {
        Timer timer = new Timer(1000); // create a Timer instance with a duration of 1000 milliseconds
        timer.start(); // start the timer
        assertTrue(timer.isCounting()); // check if the timer is still counting down
    }

    @Test
    public void testTimerIsNotCountingAfterExpiration() {
        Timer timer = new Timer(1000); // create a Timer instance with a duration of 1000 milliseconds
        timer.start(); // start the timer
        long delay = 1500; // wait for some time, more than the duration of the timer
        try {
            Thread.sleep(delay); // simulate the passage of time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting()); // check if the timer is not counting down anymore
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTimerTwice() {
        Timer timer = new Timer(1000); // create a Timer instance with a duration of 1000 milliseconds
        timer.start(); // start the timer
        timer.start(); // attempt to start the timer again, which should throw an IllegalStateException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration() {
        Timer timer = new Timer(-1000); // attempt to create a Timer instance with a negative duration, which should throw an IllegalArgumentException
    }

}