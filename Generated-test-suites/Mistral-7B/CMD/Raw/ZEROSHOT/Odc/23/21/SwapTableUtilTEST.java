package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
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
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.FINISHED;
        Double fullTransferProgressPercentage = 50.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse("Swap table is not enabled when SwapTableType is not MANUAL",
                SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, taskStatus, fullTransferProgressPercentage,
                        fullVerificationResult, manualSwapTableStarted));

        assertFalse("Swap table is not enabled when manualSwapTableStarted is true",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                        fullVerificationResult, true));

        assertFalse("Swap table is not enabled when taskStatus is not RUNNING",
                SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.PAUSED, fullTransferProgressPercentage,
                        fullVerificationResult, manualSwapTableStarted));

        assertFalse("Swap table is not enabled when fullTransferProgressPercentage is not 100",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                        fullVerificationResult, manualSwapTableStarted));

        assertFalse("Swap table is not enabled when fullVerificationResult is INCONSISTENT",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                        fullVerificationResult, manualSwapTableStarted));

        assertTrue("Swap table is enabled when all conditions are met",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, 100.0, fullVerificationResult,
                        manualSwapTableStarted));

        // Test null input
        assertNull("fullVerificationResult should be null when it is null",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, null,
                        manualSwapTableStarted));

        assertNotNull("fullVerificationResult should not be null when it is not null",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                        FullVerificationResult.CONSISTENT, manualSwapTableStarted));
    }
}