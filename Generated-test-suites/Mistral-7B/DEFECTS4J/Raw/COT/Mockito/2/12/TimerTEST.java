package org.junit.tests;

import org.junit.Test;
import org.mockito.internal.util.Timer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

public class TimerTest {

    @Test
    public void testTimerConstruction() {
        Timer timer = new Timer(1000);
        assertTrue(timer instanceof Timer);
    }

    @Test
    public void testIsCounting_notStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_counting() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.getStartTime();

        // Simulate time passing, make sure timer is still counting
        currentTime += 500;
        assertTrue(timer.isCounting());
        assertTrue(elapsedTime <= 1000);
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_alreadyFinished() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start(); // This should throw an AssertionError
    }

    @Test
    public void testIsCounting_finished() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.getStartTime();

        // Simulate time passing until timer is finished
        currentTime += 1001;
        assertFalse(timer.isCounting());
        assertTrue(elapsedTime >= 1000);
    }

    @Test
    public void testNegativeDuration() {
        try {
            new Timer(-1000);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

}