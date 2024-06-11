package org.junit.test; // replace with your test package name if needed

import org.junit.Test;
import org.mockito.internal.util.Timer;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testTimerWithValidDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerWithZeroDuration() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerWithNegativeDuration() {
        Timer timer = new Timer(-1000);
    }

    @Test
    public void testTimerReachesEnd() {
        Timer timer = new Timer(1000);
        timer.start();
        sleep(1010); // sleep for 1010ms to let timer finish counting
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerCountingDown() {
        Timer timer = new Timer(2000);
        timer.start();
        long startTime = System.currentTimeMillis();
        sleep(1000);
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(timer.isCounting());
        assertTrue(elapsedTime < timer.durationMillis);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}