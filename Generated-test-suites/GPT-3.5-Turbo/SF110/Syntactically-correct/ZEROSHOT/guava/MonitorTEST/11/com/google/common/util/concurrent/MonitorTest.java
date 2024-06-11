package com.google.common.util.concurrent;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

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
        monitor.leave();
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testEnterWithTimeout() {
        assertTrue(monitor.enter(1, TimeUnit.SECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
        assertTrue(monitor.enterInterruptibly(1, TimeUnit.SECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testTryEnter() {
        assertTrue(monitor.tryEnter());
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testEnterWhen() throws InterruptedException {
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };

        monitor.enterWhen(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };

        monitor.enterWhenUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testEnterWhenWithTimeout() throws InterruptedException {
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };

        assertTrue(monitor.enterWhen(guard, 1, TimeUnit.SECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptiblyWithTimeout() {
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };

        assertTrue(monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.SECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }
    
    //More test methods for remaining methods in Monitor class
    
}