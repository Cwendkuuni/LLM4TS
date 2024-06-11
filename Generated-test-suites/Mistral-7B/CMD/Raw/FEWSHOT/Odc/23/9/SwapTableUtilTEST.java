package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case 1: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100D, FullVerificationResult.CONSISTENT, false));

        // Test case 2: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is not 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99D, FullVerificationResult.CONSISTENT, false));

        // Test case 3: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100D, FullVerificationResult.UNCHECK, false));

        // Test case 4: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is null
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100D, null, false));

        // Test case 5: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100D, FullVerificationResult.CONSISTENT, true));

        // Test case 6: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100D, FullVerificationResult.UNCHECK, true));

        // Test case 7: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is COMPLETED, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100D, FullVerificationResult.CONSISTENT, false));

        // Test case 8: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is COMPLETED, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100D, FullVerificationResult.UNCHECK, false));

        // Test case 9: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is FAILED, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FAILED, 100D, FullVerificationResult.CONSISTENT, false));

        // Test case 10: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is FAILED, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FAILED, 100D, FullVerificationResult.UNCHECK, false));

        // Test case 11: SwapTableType is AUTO, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100D, FullVerificationResult.CONSISTENT, false));

        // Test case 12: SwapTableType is AUTO, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100D, FullVerificationResult.UNCHECK, false));

        // Test case 13: SwapTableType is AUTO, manualSwapTableStarted is true, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100D, FullVerificationResult.CONSISTENT, true));

        // Test case 14: SwapTableType is AUTO, manualSwapTableStarted is true, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100D, FullVerificationResult.UNCHECK, true));

        // Test case 15: SwapTableType is AUTO, manualSwapTableStarted is false, taskStatus is COMPLETED, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.COMPLETED, 100D, FullVerificationResult.CONSISTENT, false));

        // Test case 16: SwapTableType is AUTO, manualSwapTableStarted is false, taskStatus is COMPLETED, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.COMPLETED, 100D, FullVerificationResult.UNCHECK, false));

        // Test case 17: SwapTableType is AUTO, manualSwapTableStarted is false, taskStatus is FAILED, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.FAILED, 100D, FullVerificationResult.CONSISTENT, false));

        // Test case 18: SwapTableType is AUTO, manualSwapTableStarted is false, taskStatus is FAILED, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.FAILED, 100D, FullVerificationResult.UNCHECK, false));
    }
}