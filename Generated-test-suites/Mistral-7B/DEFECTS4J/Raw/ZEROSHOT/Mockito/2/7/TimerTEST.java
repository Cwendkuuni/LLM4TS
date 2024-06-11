package org.junit.test; // replace with your actual package name

import org.junit.Test;
import org.mockito.internal.util.Timer;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

    @Test
    public void testConstructor() {
        long durationMillis = 1000; // valid duration
        Timer timer = new Timer(durationMillis);
        assertTrue(timer.isCounting()); // new timer should be counting
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDuration() {
        Timer timer = new Timer(-1); // negative duration should throw exception
    }

    @Test
    public void testIsCountingWhenNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting()); // timer not started, not counting
    }

    @Test
    public void testIsCountingWhenStarted() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting()); // timer started, counting
    }

    @Test
    public void testIsCountingWhenExpired() {
        Timer timer = new Timer(100);
        timer.start();
        long delay = System.currentTimeMillis() + 150; // wait for 150ms
        while (System.currentTimeMillis() < delay) { // simulate delay
            timer.isCounting();
        }
        assertFalse(timer.isCounting()); // timer expired, not counting anymore
    }

}