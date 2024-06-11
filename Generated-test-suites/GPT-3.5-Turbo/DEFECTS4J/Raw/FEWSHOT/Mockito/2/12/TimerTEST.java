package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {
    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        
        assertFalse(timer.isCounting());
        
        timer.start();
        assertTrue(timer.isCounting());
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertTrue(timer.isCounting());
        
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        
        timer.start();
        assertTrue(timer.isCounting());
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertTrue(timer.isCounting());
        
        timer.start();
        assertTrue(timer.isCounting());
    }
}