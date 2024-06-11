package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class MonitorTest {
    
    private Monitor monitor;
    
    @Before
    public void setup() {
        monitor = new Monitor();
    }
    
    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }
    
    @Test
    public void testEnterInterruptibly() {
        try {
            monitor.enterInterruptibly();
            assertTrue(monitor.isOccupiedByCurrentThread());
        } catch (InterruptedException e) {
            fail("Should not throw InterruptedException");
        }
    }
    
    @Test
    public void testEnterWithTimeout() {
        boolean entered = monitor.enter(2, TimeUnit.SECONDS);
        assertTrue(entered);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }
    
    @Test
    public void testEnterInterruptiblyWithTimeout() {
        try {
            boolean entered = monitor.enterInterruptibly(2, TimeUnit.SECONDS);
            assertTrue(entered);
            assertTrue(monitor.isOccupiedByCurrentThread());
        } catch (InterruptedException e) {
            fail("Should not throw InterruptedException");
        }
    }
    
    @Test
    public void testTryEnter() {
        boolean entered = monitor.tryEnter();
        assertTrue(entered);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }
    
    //Write test cases for the all other methods in the Monitor class
    
}