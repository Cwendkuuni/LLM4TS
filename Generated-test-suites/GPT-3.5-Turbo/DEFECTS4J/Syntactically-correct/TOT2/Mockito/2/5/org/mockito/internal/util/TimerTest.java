package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingAfterDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(1500); // wait for duration to pass
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStartWithoutIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start(); // starting again should not be allowed
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStartAndIsCountingWithoutStart() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWithoutStart() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingBeforeStart() {
        Timer timer = new Timer(1000);
        timer.isCounting(); // should throw AssertionError
    }

    @Test
    public void testConstructor() {
        Timer timer = new Timer(1000);
        assertNotNull(timer);
    }
}