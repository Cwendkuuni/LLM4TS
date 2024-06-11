package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;
    private static final long DURATION = 1000;

    @Before
    public void setUp() {
        timer = new Timer(DURATION);
    }

    @Test
    public void testIsCountingWhenCounting() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenNotCounting() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }
}