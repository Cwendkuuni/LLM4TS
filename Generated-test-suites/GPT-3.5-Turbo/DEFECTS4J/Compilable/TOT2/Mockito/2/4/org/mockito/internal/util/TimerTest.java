
package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }
    
    @Test(expected = AssertionError.class)
    public void testIsCountingWhenStartTimeNotSet() {
        Timer timer = new Timer(5000);
        timer.isCounting();
    }
    
    @Test
    public void testIsCountingAfterEndTime() {
        Timer timer = new Timer(100);
        timer.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }
}

