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
    public void testIsCounting_NotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testIsCounting_AlreadyExpired() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotEquals(-1, timer.getStartTime());
    }
}