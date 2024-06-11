package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        // Typical use case: timer is still counting
        Timer timer1 = new Timer(1000);
        timer1.start();
        assertTrue(timer1.isCounting());
        
        // Typical use case: timer has finished counting
        Timer timer2 = new Timer(1000);
        timer2.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer2.isCounting());
        
        // Error scenario: timer is never started
        Timer timer3 = new Timer(1000);
        assertFalse(timer3.isCounting());
    }
    
    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }
}