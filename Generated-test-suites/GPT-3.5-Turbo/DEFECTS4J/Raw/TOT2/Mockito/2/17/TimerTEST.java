package org.mockito.internal.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class TimerTest {
    
    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotNull(timer.startTime);
    }
    
    @Test
    public void testIsCountingEdgeCase() {
        Timer timer = new Timer(1000);
        timer.start();
        // Wait for the duration of the timer to ensure isCounting returns false
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }
    
    @Test(expected = AssertionError.class)
    public void testIsCountingErrorScenario() {
        Timer timer = new Timer(1000);
        timer.isCounting(); // Calling isCounting without calling start should throw an AssertionError
    }
    
}