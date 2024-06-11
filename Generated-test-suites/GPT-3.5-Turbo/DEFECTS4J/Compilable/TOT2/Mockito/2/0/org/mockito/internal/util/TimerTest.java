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
    public void testIsCountingAfterDuration() {
        Timer timer = new Timer(2000);
        timer.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingBeforeStart() {
        Timer timer = new Timer(5000);
        timer.isCounting();
    }

}