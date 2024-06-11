package org.mockito.internal.util;

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
        // Test when timer is still counting
        timer.start();
        assertTrue(timer.isCounting());
        
        // Test when timer has finished
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        timer.start();
        assertTrue(timer.isCounting());
    }
    
}