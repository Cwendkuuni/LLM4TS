package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case 1: Swap table type is manual, manualSwapTableStarted is false, task status is running, full transfer progress percentage is 100, and full verification result is consistent
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case 2: Swap table type is manual, manualSwapTableStarted is true, task status is running, full transfer progress percentage is 100, and full verification result is consistent
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));

        // Test case 3: Swap table type is manual, manualSwapTableStarted is false, task status is not running, full transfer progress percentage is 100, and full verification result is consistent
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case 4: Swap table type is manual, manualSwapTableStarted is false, task status is running, full transfer progress percentage is not 100, and full verification result is consistent
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false));

        // Test case 5: Swap table type is manual, manualSwapTableStarted is false, task status is running, full transfer progress percentage is 100, and full verification result is uncheck
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));

        // Test case 6: Swap table type is automatic, manualSwapTableStarted is false, task status is running, full transfer progress percentage is 100, and full verification result is consistent
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case 7: Swap table type is automatic, manualSwapTableStarted is false, task status is running, full transfer progress percentage is 100, and full verification result is uncheck
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));

        // Test case 8: Swap table type is null, manualSwapTableStarted is false, task status is running, full transfer progress percentage is 100, and full verification result is null
        assertNull(SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0, null, false));

        // Test case 9: Swap table type is manual, manualSwapTableStarted is false, task status is null, full transfer progress percentage is 100, and full verification result is consistent
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case 10: Swap table type is manual, manualSwapTableStarted is false, task status is running, full transfer progress percentage is 100, and full verification result is unchecked, and full verification result is not initialized
        FullVerificationResult fullVerificationResult = null;
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, fullVerificationResult, false));
    }

    // Add other test cases as needed

    @Test(expected = NullPointerException.class)
    public void testNullArgument() {
        SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSwapTableType() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.INVALID, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTaskStatus() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.INVALID, 100.0, FullVerificationResult.CONSISTENT, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeProgressPercentage() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, -1.0, FullVerificationResult.CONSISTENT, false);
    }
}