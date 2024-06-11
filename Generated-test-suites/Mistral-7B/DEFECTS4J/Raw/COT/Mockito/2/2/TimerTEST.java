package org.junit.platform.commons.util.Timer; // Assuming the test file is located in this package for import purposes

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

    @Test
    public void testTimerConstructor() {
        Timer timer = new Timer(1000);
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_TimerNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_TimerExpired() {
        Timer timer = new Timer(100L);
        timer.start();
        long waitTime = timer.durationMillis - 1;
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_TimerNotInitialized() {
        Timer timer = new Timer(-1);
        timer.isCounting();
    }

    @Test
    public void testStart_TimerNotRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_TimerRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testStart_TimerExpired() {
        Timer timer = new Timer(100L);
        timer.start();
        timer.start();
        try {
            Thread.sleep(timer.durationMillis + 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.start();
    }

    @Test
    public void testIsCounting_TimerPaused() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.pause();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_TimerPausedAndResumed() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.pause();
        timer.resume();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testPause_TimerNotRunning() {
        Timer timer = new Timer(1000);
        timer.pause();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testPause_TimerRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.pause();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testResume_TimerPaused() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.pause();
        timer.resume();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testResume_TimerNotPaused() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.resume();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerWithZeroDuration() {
        Timer timer = new Timer(0);
        timer.start();
        // The test would fail because the timer should not be started with a zero duration
        // However, it's worth adding a test for such cases to ensure proper error handling in case the Timer is initialized with a zero duration
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

}