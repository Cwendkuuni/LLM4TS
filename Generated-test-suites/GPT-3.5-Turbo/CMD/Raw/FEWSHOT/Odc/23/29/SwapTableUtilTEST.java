package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case for swapTableType == MANUAL, manualSwapTableStarted == false, taskStatus == RUNNING,
        // fullTransferProgressPercentage == 100, fullVerificationResult == CONSISTENT
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case for swapTableType == MANUAL, manualSwapTableStarted == true, taskStatus == RUNNING,
        // fullTransferProgressPercentage == 100, fullVerificationResult == CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));

        // Test case for swapTableType == AUTO, manualSwapTableStarted == false, taskStatus == RUNNING,
        // fullTransferProgressPercentage == 100, fullVerificationResult == CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case for swapTableType == MANUAL, manualSwapTableStarted == false, taskStatus == FAILED,
        // fullTransferProgressPercentage == 100, fullVerificationResult == CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FAILED, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case for swapTableType == MANUAL, manualSwapTableStarted == false, taskStatus == RUNNING,
        // fullTransferProgressPercentage == 99, fullVerificationResult == CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false));

        // Test case for swapTableType == MANUAL, manualSwapTableStarted == false, taskStatus == RUNNING,
        // fullTransferProgressPercentage == 100, fullVerificationResult == UNCHECK
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));
    }
}