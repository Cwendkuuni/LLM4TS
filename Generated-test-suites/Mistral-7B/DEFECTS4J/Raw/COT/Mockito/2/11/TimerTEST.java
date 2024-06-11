package org.junit.platform.commons.annotation. reveorter.utils.timer; // Assuming this is the package for your JUnit 4 test file

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerConstruction() {
        Timer timer = new Timer(1000);
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerExpired() {
        Timer timer = new Timer(1000);
        timer.start();
        long delay = 1500; // 1.5 seconds
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingWhenStartTimeIsInvalid() {
        Timer timer = new Timer(1000);
        timer.startTime = 0;
        timer.isCounting();
    }

    @Test
    public void testStartMethod() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartMethodWhenTimerIsAlreadyRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

}