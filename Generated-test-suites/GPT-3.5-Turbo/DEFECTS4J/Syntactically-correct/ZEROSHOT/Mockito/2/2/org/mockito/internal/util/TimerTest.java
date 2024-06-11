package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);
        
        // Test when timer is not started yet
        assertFalse(timer.isCounting());
        
        // Test when timer has started, but duration has not passed
        timer.start();
        assertTrue(timer.isCounting());
        
        // Test when timer has started and duration has passed
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        
        // Test when timer is not started yet
        assertEquals(-1, timer.startTime);
        
        // Test when timer has started
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }

}