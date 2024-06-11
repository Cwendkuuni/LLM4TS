package org.mockito.internal.util;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.Timer;

public class TimerTest {
    private Timer timer;
    
    @Before
    public void setup() {
        timer = new Timer(5000);
    }
    
    @Test
    public void testIsCountingWhenNotStarted() {
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testIsCountingWhileCounting() throws InterruptedException {
        timer.start();
        Thread.sleep(1000);
        assertTrue(timer.isCounting());
    }
    
    @Test
    public void testIsCountingWhenFinished() throws InterruptedException {
        timer.start();
        Thread.sleep(6000);
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        timer.start();
        assertTrue(timer.isCounting());
    }
}