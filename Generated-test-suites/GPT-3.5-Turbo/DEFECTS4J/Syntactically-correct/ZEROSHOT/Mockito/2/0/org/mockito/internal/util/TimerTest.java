package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {
    private Timer timer;
    
    @Before
    public void setUp() {
        timer = new Timer(1000);
    }
    
    @Test
    public void testIsCounting() {
        timer.start();
        
        assertTrue(timer.isCounting());
        
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