package org.mockito.internal.util;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class TimerTest {

    @Mock
    private Timer timer;

    public TimerTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsCounting() {
        timer = Mockito.spy(new Timer(1000));
        timer.start();

        assertTrue(timer.isCounting());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer = new Timer(1000);
        timer.start();

        assertNotNull(timer);
        assertNotEquals(-1, timer.startTime);
    }
}