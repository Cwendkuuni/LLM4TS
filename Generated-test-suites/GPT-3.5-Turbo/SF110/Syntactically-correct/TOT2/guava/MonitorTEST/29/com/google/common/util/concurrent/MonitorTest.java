package com.google.common.util.concurrent;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.concurrent.TimeUnit;

public class MonitorTest {

    private Monitor monitor;

    @Before
    public void setUp() {
        monitor = new Monitor();
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWithTimeout() {
        assertTrue(monitor.enter(1, TimeUnit.SECONDS));
    }

    @Test
    public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
        assertTrue(monitor.enterInterruptibly(1, TimeUnit.SECONDS));
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testLeave() {
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

}