package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Case 1: SwapTableType is not MANUAL, manualSwapTableStarted is false
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 50d, FullVerificationResult.UNCHECKED, false));

        // Case 2: SwapTableType is MANUAL, manualSwapTableStarted is true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50d, FullVerificationResult.UNCHECKED, true));

        // Case 3: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100d, FullVerificationResult.CONSISTENT, false));

        // Case 4: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is not 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50d, FullVerificationResult.UNCHECKED, false));

        // Case 5: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));

        // Case 6: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, false));
    }
}