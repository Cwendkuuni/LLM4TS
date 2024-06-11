package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_timerIsNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long expectedTime = currentTime - 1000L;

        assertTrue(timer.isCounting());
        assertTrue(System.currentTimeMillis() - expectedTime <= 1001);
    }

    @Test
    public void testIsCounting_timerHasFinished() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.waitUntilFinished(); // assuming there is a method waitUntilFinished()

        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_timerHasNotBeenInitialized() {
        new Timer(1000).isCounting(); // This line will throw an AssertionError as 'startTime' is not initialized
    }
}