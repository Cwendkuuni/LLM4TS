package com.google.common.util.concurrent;

import org.junit.Test;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

public class MonitorTest {

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWithTimeout() {
        Monitor monitor = new Monitor();
        boolean entered = monitor.enter(1, TimeUnit.SECONDS);
        assertTrue(entered);
    }

    // Add more test cases for other methods

}