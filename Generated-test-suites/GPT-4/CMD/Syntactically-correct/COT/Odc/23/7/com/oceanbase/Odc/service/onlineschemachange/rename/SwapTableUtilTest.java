package com.oceanbase.odc.service.onlineschemachange.rename;
import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {

        // Basic functionality test
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Edge Cases
        
        // Null input handling for enumerated types
        try {
            SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expecting this exception as response.
        }

        // Maximum border value
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.1, FullVerificationResult.CONSISTENT, false));
        
        // ManualSwapTableStarted being true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
    }
}