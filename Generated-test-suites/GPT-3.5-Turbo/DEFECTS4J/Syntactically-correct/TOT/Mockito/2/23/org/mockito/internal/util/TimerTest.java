package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(5000);
    }

    @Test
    public void testIsCounting() {
        // Typical use case
        timer.start();
        assertTrue(timer.isCounting());

        // Edge case: exactly at the end of the duration
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());

        // Error scenario: timer not started
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        // Typical use case
        timer.start();
        assertTrue(timer.isCounting());

        // Error scenario: timer already started
        timer.start();
        assertTrue(timer.isCounting());
    }

}