package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting() {
        // Test when timer is not started
        assertFalse(timer.isCounting());

        // Test when timer is started
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer.start();
        assertEquals(System.currentTimeMillis(), timer.startTime);
    }

}