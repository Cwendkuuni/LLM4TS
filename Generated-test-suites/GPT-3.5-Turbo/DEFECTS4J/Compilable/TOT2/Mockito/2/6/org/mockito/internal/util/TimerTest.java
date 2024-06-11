package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {
    
    private Timer timer;
    
    @Test
    public void testIsCountingTrue() {
        timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }
    
    @Test
    public void testIsCountingFalse() {
        timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testStartTimer() {
        timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

}