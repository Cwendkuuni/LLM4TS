package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {
    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting() {
        timer.start();
        assertTrue(timer.isCounting());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(timer.isCounting());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer.start();
        assertEquals(System.currentTimeMillis(), timer.startTime, 500);
    }
}