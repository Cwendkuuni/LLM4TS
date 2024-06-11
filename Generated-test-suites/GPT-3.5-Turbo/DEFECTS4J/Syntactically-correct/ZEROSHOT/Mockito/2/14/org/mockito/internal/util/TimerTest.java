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
        timer = new Timer(5000);
    }

    @Test
    public void testIsCounting() {
        assertFalse(timer.isCounting());
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer.start();
        assertEquals(System.currentTimeMillis(), timer.getStartTime());
    }

}