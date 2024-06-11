package org.mockito.internal.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(10000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerNotStarted() {
        Timer timer = new Timer(10000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerExceedsDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(10000);
        timer.start();
        assertTrue(timer.isCounting());
    }

}