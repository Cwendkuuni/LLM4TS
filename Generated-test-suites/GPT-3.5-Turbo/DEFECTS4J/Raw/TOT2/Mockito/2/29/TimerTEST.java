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
    public void testIsCounting_false() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }

    @Test
    public void testStartTwice() {
        Timer timer = new Timer(1000);
        timer.start();
        long firstStartTime = timer.startTime;
        timer.start();
        long secondStartTime = timer.startTime;
        assertNotEquals(firstStartTime, secondStartTime);
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_noStart() {
        Timer timer = new Timer(1000);
        timer.isCounting();
    }

    @Test
    public void testIsCounting_expired() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(1100); // wait for timer to expire
        assertFalse(timer.isCounting());
    }

}