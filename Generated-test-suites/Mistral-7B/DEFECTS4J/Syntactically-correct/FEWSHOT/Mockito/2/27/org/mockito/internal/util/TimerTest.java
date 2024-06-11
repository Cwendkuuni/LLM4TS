package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_timerNotStarted() {
        Timer timer = new Timer(1000L);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerRunning() {
        Timer timer = new Timer(1000L);
        timer.start();
        long startMillis = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        long elapsedMillis = System.currentTimeMillis() - startMillis;
        assert False(timer.isCounting() || elapsedMillis > timer.durationMillis);
    }

    @Test
    public void testIsCounting_timerExpired() {
        Timer timer = new Timer(1L);
        timer.start();
        long startMillis = System.currentTimeMillis();
        long elapsedMillis = System.currentTimeMillis() - startMillis;
        assertFalse(timer.isCounting() || elapsedMillis <= timer.durationMillis);
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_timerNotInitialized() {
        Timer timer = new Timer(-1L);
        timer.isCounting();
    }

    @Test
    public void testStart_timerAlreadyRunning() {
        Timer timer = new Timer(1000L);
        timer.start();
        timer.start();
    }

    @Test
    public void testStart_timerNotRunning() {
        Timer timer = new Timer(1000L);
        timer.start();
        long startMillis = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        long elapsedMillis = System.currentTimeMillis() - startMillis;
        assertTrue(elapsedMillis <= timer.durationMillis);
    }
}