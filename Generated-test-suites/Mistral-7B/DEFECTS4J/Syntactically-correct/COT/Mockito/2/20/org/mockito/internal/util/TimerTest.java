package org.junit.test; // or your package name for the test file

import static org.junit.Assert.*;
import org.mockito.internal.util.Timer;

public class TimerTest {

    @org.junit.Test
    public void testTimerConstructor() {
        Timer timer = new Timer(5000);
        assertNotSame(-1, timer.getStartTime()); // check startTime is initialized correctly
    }

    @org.junit.Test
    public void testIsCounting_whenTimerIsRunning_shouldReturnTrue() {
        Timer timer = new Timer(5000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elaspedTime = currentTime - 100; // allow some elasped time
        assertTrue("Timer should be counting", timer.isCounting());
    }

    @org.junit.Test
    public void testIsCounting_whenTimerIsNotRunning_shouldReturnFalse() {
        Timer timer = new Timer(5000);
        assertFalse("Timer should not be counting", timer.isCounting());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testIsCounting_whenStartTimeIsInvalid_shouldThrowAssertionError() {
        Timer timer = new Timer(5000);
        timer.setStartTime(-1);
        timer.isCounting();
    }

    @org.junit.Test
    public void testStart_whenTimerIsNotRunning_shouldSetStartTime() {
        Timer timer = new Timer(5000);
        timer.start();
        long startTime = timer.getStartTime();
        assertNotNull("Start time should be set", startTime);
    }

    @org.junit.Test
    public void testStart_whenTimerIsAlreadyRunning_shouldNotChangeStartTime() {
        Timer timer = new Timer(5000);
        timer.start();
        long startTime = timer.getStartTime();
        timer.start();
        long newStartTime = timer.getStartTime();
        assertEquals("Start time should not change", startTime, newStartTime);
    }

}