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
        // fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case 2: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is UNCHECK
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));

        // Test case 3: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));

        // Test case 4: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, true));

        // Test case 5: SwapTableType is not MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.OTHER, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case 6: SwapTableType is not MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.OTHER, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));

        // Test case 7: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is NOT RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case 8: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is NOT RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.UNCHECK, false));

        // Test case 9: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is NOT 100, and fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false));

        // Test case 10: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is NOT 100, and fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.UNCHECK, false));
    }

}