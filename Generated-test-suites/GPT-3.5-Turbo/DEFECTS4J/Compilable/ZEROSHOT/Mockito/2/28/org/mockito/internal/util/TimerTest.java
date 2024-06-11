package org.mockito.internal.util;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting_Stop() {
        Timer timer = new Timer(1000);
        timer.start();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_Continue() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

}