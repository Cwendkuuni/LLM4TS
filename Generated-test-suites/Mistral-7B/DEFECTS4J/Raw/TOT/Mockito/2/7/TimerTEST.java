package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_TimerIsRunning() {
        // Given
        Timer timer = new Timer(1000);
        timer.start();

        // When
        long currentTime = System.currentTimeMillis();

        // Then
        long elapsedTime = currentTime - timer.getStartTime();
        Assert.assertTrue("Timer should be counting", timer.isCounting());
        Assert.assertTrue("Elapsed time should be less than or equal to duration", elapsedTime <= timer.getDurationMillis());
    }

    // Test other methods here

    
    // Your complete test file should be added here
    
}