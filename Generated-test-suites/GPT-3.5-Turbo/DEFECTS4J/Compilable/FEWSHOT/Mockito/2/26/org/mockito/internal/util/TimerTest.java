
package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        // Test case where timer is still counting
        Timer timer1 = new Timer(1000);
        timer1.start();
        // Sleep for less than the duration
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(timer1.isCounting());
        
        // Test case where timer is not counting anymore
        Timer timer2 = new Timer(1000);
        timer2.start();
        // Sleep for more than the duration
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer2.isCounting());
    }

    @Test
    public void testStart() {
        // Test case for starting timer
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }
}
