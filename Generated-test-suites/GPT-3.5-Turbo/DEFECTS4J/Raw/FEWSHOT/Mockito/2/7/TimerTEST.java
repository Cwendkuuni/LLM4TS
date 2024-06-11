
package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

    @Test
    public void testIsCounting() {
        // Test when timer is still counting down
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());

        // Test when timer has finished counting down
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }
}
