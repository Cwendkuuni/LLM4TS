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
        // Test case 1: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case 2: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));

        // Test case 3: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));

        // Test case 4: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is INCONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));

        // Test case 5: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is COMPLETED,
        // fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case 6: SwapTableType is NOT MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.BACKUP, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
    }
}