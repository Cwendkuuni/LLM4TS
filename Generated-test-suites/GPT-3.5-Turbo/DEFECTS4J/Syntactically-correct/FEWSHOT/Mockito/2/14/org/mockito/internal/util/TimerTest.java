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
    }
    
    @Test
    public void testIsCounting_False() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(System.currentTimeMillis() - timer.startTime < 1000);
    }
    
    @Test
    public void testStart_StartTimeEqualsCurrentTime() {
        Timer timer = new Timer(1000);
        timer.start();
        assertEquals(System.currentTimeMillis(), timer.startTime);
    }
}