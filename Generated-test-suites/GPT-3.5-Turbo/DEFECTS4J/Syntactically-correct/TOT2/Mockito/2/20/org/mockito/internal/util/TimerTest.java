package org.mockito.internal.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCountingTrue() {
        Timer timer = new Timer(5000); // 5 seconds
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingFalse() {
        Timer timer = new Timer(1000); // 1 second
        timer.start();
        try {
            Thread.sleep(1500); // Wait for 1.5 seconds
        } catch (InterruptedException e) {
            fail("Interrupted while sleeping");
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(2000); // 2 seconds
        timer.start();
        assertTrue(timer.isCounting());
    }

}