package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingEdgeCase() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        assertNotNull(timer);
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingWhenNotStarted() {
        Timer timer = new Timer(5000);
        assertFalse(timer.isCounting());
    }

}