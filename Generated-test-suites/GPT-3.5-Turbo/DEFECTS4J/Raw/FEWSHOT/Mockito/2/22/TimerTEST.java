package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {
    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());

        timer = new Timer(2000);
        timer.start();
        assertTrue(timer.isCounting());

        timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());

        timer = new Timer(-1000);
        timer.start();
        assertFalse(timer.isCounting());

        timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());

        timer.start();
        assertTrue(timer.isCounting());

        timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());

        timer = new Timer(-1000);
        timer.start();
        assertFalse(timer.isCounting());
    }
}