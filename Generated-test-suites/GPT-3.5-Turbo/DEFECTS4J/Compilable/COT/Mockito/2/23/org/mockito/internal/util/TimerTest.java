
package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {
  
    private Timer timer;
    
    @Before
    public void setUp() {
        // Initialize Timer with duration of 1000 milliseconds (1 second)
        timer = new Timer(1000);
    }
    
    @Test
    public void testIsCounting_StartNotCalled() {
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testIsCounting_StartCalled() {
        timer.start();
        assertTrue(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        timer.start();
        assertTrue(timer.isCounting());
    }
    
    @Test
    public void testIsCounting_DurationExpires() throws InterruptedException {
        timer.start();
        // Sleep for 2000 milliseconds (2 seconds) to let the duration expire
        Thread.sleep(2000);
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testIsCounting_JustBeforeDurationExpires() throws InterruptedException {
        timer.start();
        // Sleep for 900 milliseconds to make the duration just before expiry
        Thread.sleep(900);
        assertTrue(timer.isCounting());
    }
}
