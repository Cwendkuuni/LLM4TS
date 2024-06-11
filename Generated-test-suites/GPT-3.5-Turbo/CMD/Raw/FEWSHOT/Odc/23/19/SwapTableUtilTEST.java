package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult1 = FullVerificationResult.CONSISTENT;
        FullVerificationResult fullVerificationResult2 = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;

        // Test with all conditions met
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));

        // Test with manualSwapTableStarted true
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, true));

        // Test with taskStatus not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.COMPLETED, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));

        // Test with fullTransferProgressPercentage not 100.0
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, 99.0, fullVerificationResult1, manualSwapTableStarted));

        // Test with fullVerificationResult not CONSISTENT or UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, FullVerificationResult.INCONSISTENT, manualSwapTableStarted));
    }
}