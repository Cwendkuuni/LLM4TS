package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {
    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting() {
        timer.start();
        assertTrue(timer.isCounting());

        try {
            Thread.sleep(2000); // Sleep for 2 seconds to ensure timer is no longer counting
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer.start();
        long startTime = timer.getStartTime();
        
        assertEquals(System.currentTimeMillis(), startTime, 1000); // Allow for a 1-second difference when comparing start time and current system time
    }
}