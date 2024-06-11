package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_notStarted() {
        Timer timer = new Timer(1000L);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_started_lessThanDuration() {
        Timer timer = new Timer(1000L);
        timer.start();
        long elapsedMillis = System.currentTimeMillis() - timer.startTime;
        assertTrue(timer.isCounting());
        assertTrue(elapsedMillis < timer.durationMillis);
    }

    @Test
    public void testIsCounting_started_equalDuration() {
        Timer timer = new Timer(1000L);
        timer.start();
        long millisToSleep = timer.durationMillis - (System.currentTimeMillis() - timer.startTime);
        try {
            Thread.sleep(millisToSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(timer.isCounting());
        assertEquals(0, timer.durationMillis - (System.currentTimeMillis() - timer.startTime));
    }

    @Test
    public void testIsCounting_started_greaterThanDuration() {
        Timer timer = new Timer(1000L);
        timer.start();
        long millisToSleep = timer.durationMillis + (System.currentTimeMillis() - timer.startTime);
        try {
            Thread.sleep(millisToSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }
}