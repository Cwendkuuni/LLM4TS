package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case 1: SwapTableType is not MANUAL, manualSwapTableStarted is false, taskStatus is not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.COMPLETED, 50d, FullVerificationResult.INCONSISTENT, false));

        // Test case 2: SwapTableType is not MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is not 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 80d, FullVerificationResult.CONSISTENT, false));

        // Test case 3: SwapTableType is not MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is INCONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100d, FullVerificationResult.INCONSISTENT, false));

        // Test case 4: SwapTableType is not MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, false));

        // Test case 5: SwapTableType is MANUAL, manualSwapTableStarted is true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, true));

        // Test case 6: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100d, FullVerificationResult.CONSISTENT, false));

        // Test case 7: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));

        // Test case 8: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, false));
    }
}