package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TimerTest {

    @Test
    public void testIsCountingTrue() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingFalse() {
        Timer timer = new Timer(5000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

}