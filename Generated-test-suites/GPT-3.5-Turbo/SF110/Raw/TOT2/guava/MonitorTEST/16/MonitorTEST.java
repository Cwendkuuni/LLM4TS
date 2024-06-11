package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;

public class MonitorTest {

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() {
        Monitor monitor = new Monitor();
        try {
            monitor.enterInterruptibly();
            assertTrue(monitor.isOccupiedByCurrentThread());
        } catch (InterruptedException e) {
            fail("InterruptedException caught unexpectedly");
        }
    }

    @Test
    public void testEnterWithTimeout() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.enter(100, TimeUnit.MILLISECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    // Add more test cases for other methods

}