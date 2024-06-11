
package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {
    
    @Test
    public void testIsCounting() {
        // Test when the timer is still counting
        Timer timer1 = new Timer(5000);
        timer1.start();
        assertTrue(timer1.isCounting());
        
        // Test when the timer has completed counting
        Timer timer2 = new Timer(1000);
        timer2.start();
        assertFalse(timer2.isCounting());
    }
    
    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

}
