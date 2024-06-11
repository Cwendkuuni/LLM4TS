
package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_WhenTimerIsCounting_ShouldReturnTrue() {
        Timer timer = new Timer(10000);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerIsNotStarted_ShouldAssertError() {
        Timer timer = new Timer(10000);
        boolean assertErrorThrown = false;
        try {
            timer.isCounting();
        } catch (AssertionError e) {
            assertErrorThrown = true;
        }
        Assert.assertTrue(assertErrorThrown);
    }

    @Test
    public void testIsCounting_WhenTimerHasExpired_ShouldReturnFalse() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(2000);
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_ShouldSetStartTimeWithCurrentTime() {
        Timer timer = new Timer(10000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        Assert.assertTrue(timer.getStartTime() >= currentTime);
    }
}
