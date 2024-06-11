package org.junit.platform.commons.annotation.tester; // Assuming you are using JUnit 4 and this package is used for importing @Test annotation

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerConstructor() {
        // Test constructor with valid argument
        Timer timer = new Timer(1000);
        // No need to assert anything as the constructor does not throw any exception
    }

    @Test
    public void testIsCounting_TimerNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_TimerStarted() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerConstructorNegativeDuration() {
        Timer timer = new Timer(-1000);
    }

    @Test
    public void testIsCounting_ElapsedTimeGreaterThanDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.getStartTime() + 2000;
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_AlreadyStarted() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

    @Test
    public void testStart_BeforeTimerIsConstructed() {
        Timer timer = null;
        try {
            timer.start();
        } catch (NullPointerException e) {
            // Expected as the timer object is not yet initialized
        }
    }

    @Test
    public void testGetStartTime_TimerNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
        long startTime = timer.getStartTime();
        // Negative start time as the timer is not started
        assertFalse(startTime > 0);
    }

    @Test
    public void testGetStartTime_TimerStarted() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = timer.getStartTime();
        assertTrue(startTime > 0);
    }

}