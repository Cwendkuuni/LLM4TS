package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case 1: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0d, FullVerificationResult.CONSISTENT, false));

        // Test case 2: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0d, FullVerificationResult.CONSISTENT, true));

        // Test case 3: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0d, FullVerificationResult.UNCHECK, false));

        // Test case 4: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT, fullVerificationResult is null
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0d, null, false));

        // Test case 5: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT, fullTransferProgressPercentage is null
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, FullVerificationResult.CONSISTENT, false));

        // Test case 6: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is not 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0d, FullVerificationResult.CONSISTENT, false));

        // Test case 7: SwapTableType is not MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.REPLICA, TaskStatus.RUNNING, 100.0d, FullVerificationResult.CONSISTENT, false));
    }
}