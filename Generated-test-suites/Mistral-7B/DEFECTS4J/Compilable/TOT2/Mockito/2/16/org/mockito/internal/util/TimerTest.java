package org.mockito.internal.util;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Timer;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }
}