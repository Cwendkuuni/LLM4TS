package org.mockito.internal.util;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting_WhenTimerNotStarted_ReturnsFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndNotExpired_ReturnsTrue() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndExpired_ReturnsFalse() throws InterruptedException {
        timer.start();
        Thread.sleep(2000); // Wait for the timer to expire
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_StartsTheTimer() {
        timer.start();
        assertTrue(timer.isCounting());
    }

}