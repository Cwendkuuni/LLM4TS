package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.CONSISTENT, false));
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.UNCHECK, false));

        // Edge cases
        // edge case: fullTransferProgressPercentage is less than 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9,
            FullVerificationResult.CONSISTENT, false));
        // edge case: taskStatus is not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.NOT_RUNNING, 100.0,
            FullVerificationResult.CONSISTENT, false));
        // edge case: fullVerificationResult is not CONSISTENT or UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.NOT_CONSISTENT_OR_UNCHECK, false));
        // edge case: manualSwapTableStarted is true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.CONSISTENT, true));

        // Exception scenarios
        // exception scenario: swapTableType is null
        try {
            SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.CONSISTENT, false);
            fail("Expected an IllegalArgumentException for null swapTableType");
        } catch (IllegalArgumentException e) {
            // Success
        }
        // exception scenario: taskStatus is null
        try {
            SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, 100.0,
            FullVerificationResult.CONSISTENT, false);
            fail("Expected an IllegalArgumentException for null taskStatus");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }
}