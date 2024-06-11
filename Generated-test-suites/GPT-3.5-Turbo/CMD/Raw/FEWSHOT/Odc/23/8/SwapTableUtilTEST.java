package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;

public class SwapTableUtilTest {
    
    @Test
    public void testIsSwapTableEnable() {
        // Test case where swapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case where swapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));

        // Test case where swapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is FAILED,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FAILED, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case where swapTableType is AUTO, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case where swapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 50, and fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false));

        // Test case where swapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is UNCHECK
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));
    }
}