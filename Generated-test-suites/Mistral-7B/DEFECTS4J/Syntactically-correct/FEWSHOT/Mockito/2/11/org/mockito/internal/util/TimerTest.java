package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(500);
    }

    @Test
    public void testIsCounting_notStarted() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_startedAndNotExpired() {
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.getStartTime();
        assertTrue(timer.isCounting());
        assertTrue(elapsedTime <= timer.getDurationMillis());
    }

    @Test
    public void testIsCounting_startedAndExpired() {
        timer.start();
        long waitTime = timer.getDurationMillis() + 100;
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCounting_notStartedAndCalled() {
        assertFalse(timer.isCounting());
        // This should throw an exception
        timer.isCounting();
    }
}