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
        // Test case 1: Swap table type is manual, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case 2: Swap table type is automatic, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));

        // Test case 3: Swap table type is manual, manualSwapTableStarted is true, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));

        // Test case 4: Swap table type is manual, manualSwapTableStarted is false, taskStatus is COMPLETED,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case 5: Swap table type is manual, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 50, and fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false));

        // Test case 6: Swap table type is manual, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, and fullVerificationResult is INCONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));
    }
}