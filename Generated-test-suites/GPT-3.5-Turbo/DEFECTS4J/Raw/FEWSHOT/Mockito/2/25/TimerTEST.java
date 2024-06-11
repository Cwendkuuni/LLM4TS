package org.mockito.internal.util;

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
        // Test timer is counting when started
        timer.start();
        assertTrue(timer.isCounting());
        
        // Test timer is not counting when not started
        timer = new Timer(5000);
        assertFalse(timer.isCounting());
        
        // Test timer is not counting after duration has passed
        timer.start();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        // Test timer has started
        timer.start();
        assertTrue(timer.isCounting());
        
        // Test start time has been set
        assertTrue(timer.getStartTime() > 0);
    }
}