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

    // Additional test cases for other methods can be added here

}