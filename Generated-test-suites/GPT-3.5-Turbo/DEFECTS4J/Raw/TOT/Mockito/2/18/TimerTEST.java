package org.mockito.internal.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimerTest {

    @Test
    public void testIsCounting_WhenTimerIsCounting_ShouldReturnTrue() {
        // Arrange
        Timer timer = new Timer(1000);
        timer.start();
        
        // Act
        boolean isCounting = timer.isCounting();
        
        // Assert
        Assertions.assertTrue(isCounting);
    }
    
    @Test
    public void testIsCounting_WhenTimerHasNotStarted_ShouldReturnFalse() {
        // Arrange
        Timer timer = new Timer(1000);
        
        // Act
        boolean isCounting = timer.isCounting();
        
        // Assert
        Assertions.assertFalse(isCounting);
    }
    
    @Test
    public void testIsCounting_WhenTimerHasFinishedCounting_ShouldReturnFalse() throws InterruptedException {
        // Arrange
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(1000); // Wait for timer to finish
        
        // Act
        boolean isCounting = timer.isCounting();
        
        // Assert
        Assertions.assertFalse(isCounting);
    }
    
    @Test
    public void testStart_ShouldSetStartTimeToSystemCurrentTimeMillis() {
        // Arrange
        Timer timer = new Timer(1000);
        
        // Act
        timer.start();
        long startTime = timer.startTime;
        
        // Assert
        Assertions.assertNotEquals(-1, startTime);
    }
}