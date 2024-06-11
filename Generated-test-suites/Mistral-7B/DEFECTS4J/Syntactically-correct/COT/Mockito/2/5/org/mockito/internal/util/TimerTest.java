package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerConstructorWithValidDurationMillis() {
        Timer timer = new Timer(1000);
        assertNotNull(timer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerConstructorWithNegativeDurationMillis() {
        Timer timer = new Timer(-1000);
    }

    @Test
    public void testIsCountingWhenTimerIsNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        long timeElapsed = 500;
        long currentTime = System.currentTimeMillis();
        long expectedEndTime = currentTime + durationMillis - timeElapsed;

        assertTrue(timer.isCounting());
        assertTrue(System.currentTimeMillis() < expectedEndTime);
    }

    @Test
    public void testIsCountingWhenTimerHasReachedEnd() {
        Timer timer = new Timer(1000);
        timer.start();
        long timeElapsed = durationMillis;

        assertTrue(timer.isCounting());
        assertTrue(System.currentTimeMillis() >= expectedEndTime);
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCountingWhenTimerHasNotBeenStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
        timer.isCounting();
    }

    @Test
    public void testStartMethod() {
        Timer timer = new Timer(1000);
        timer.start();
        long timeElapsed = 500;

        long expectedStartTime = System.currentTimeMillis() - timeElapsed;
        long startTimeActual = timer.getStartTime();

        assertEquals(expectedStartTime, startTimeActual);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartMethodWhenTimerIsAlreadyRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

}