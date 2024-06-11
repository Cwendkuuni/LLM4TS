package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        // Initialize timer with duration of 1000 milliseconds
        Timer timer = new Timer(1000);
        
        // Test when timer has just started
        timer.start();
        assertTrue(timer.isCounting());
        
        // Test when timer has not started yet
        assertFalse(timer.isCounting());
        
        // Test when timer has reached the duration
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
        
        // Test when timer is still counting down
        timer.start();
        assertTrue(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        // Initialize timer with duration of 1000 milliseconds
        Timer timer = new Timer(1000);
        
        // Test when starting the timer
        timer.start();
        assertNotEquals(-1, timer.getStartTime());
    }
    
}