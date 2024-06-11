package com.oceanbase.odc.service.onlineschemachange.rename.test;

import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case 1: SwapTableType is MANUAL, taskStatus is RUNNING, fullTransferProgressPercentage is 100,
        // FullVerificationResult is CONSISTENT, and manualSwapTableStarted is false
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));

        // Test case 2: SwapTableType is not MANUAL, or manualSwapTableStarted is true, or taskStatus is not RUNNING,
        // or fullTransferProgressPercentage is not 100, or FullVerificationResult is not CONSISTENT and UNCHECK
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.COMPLETED, 50d, FullVerificationResult.INCONSISTENT, true) == false);
    }

    // Add more test cases for other edge cases and exception handling scenarios based on the SwapTableUtil class

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnableWithNullArguments() {
        // Test case to check if the exception is thrown when any of the arguments is null
        SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSwapTableEnableWithInvalidSwapTableType() {
        // Test case to check if the exception is thrown when an invalid SwapTableType is passed
        SwapTableUtil.isSwapTableEnable(SwapTableType.INVALID, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false);
    }

    // Add more test cases for other edge cases and exception handling scenarios based on the SwapTableUtil class

}