package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting_WhenTimerNotStarted() {
        Timer timer = new Timer(1000); // Duration of 1 second
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndNotExpired() {
        Timer timer = new Timer(1000); // Duration of 1 second
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndExpired() throws InterruptedException {
        Timer timer = new Timer(1000); // Duration of 1 second
        timer.start();
        Thread.sleep(1100); // Sleep for more than 1 second
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000); // Duration of 1 second
        timer.start();
        assertTrue(timer.isCounting());
    }

}