package org.mockito.internal.util;
import org.junit.Test;
import org.mockito.internal.util.Timer;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }
}