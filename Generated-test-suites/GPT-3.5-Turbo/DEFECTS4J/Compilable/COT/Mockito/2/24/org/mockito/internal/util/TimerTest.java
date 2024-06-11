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

    @Test
    public void testIsCounting_NotStarted() {
        Timer timer = new Timer(5000);
        
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_Expired() throws InterruptedException {
        Timer timer = new Timer(100);
        timer.start();

        Thread.sleep(200);

        assertFalse(timer.isCounting());
    }

}