
package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {
    
    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        
        try {
            Thread.sleep(500); // Wait for 500 milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Assert.assertTrue(timer.isCounting());
        
        try {
            Thread.sleep(600); // Wait for another 600 milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Assert.assertFalse(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        
        Assert.assertTrue(timer.isCounting());
    }
}
