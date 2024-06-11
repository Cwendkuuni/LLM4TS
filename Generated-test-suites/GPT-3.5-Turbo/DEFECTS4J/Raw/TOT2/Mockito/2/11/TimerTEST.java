package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());

        // Edge case: timer just started
        assertFalse(timer.isCounting());

        // Edge case: timer just finished
        try {
            Thread.sleep(5001);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());

        // Error scenario: trying to start timer again before it finishes
        timer.start();
        assertTrue(timer.isCounting());
    }
}