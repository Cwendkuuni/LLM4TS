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
        // Test case where SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case where SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));

        // Test case where SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));

        // Test case where SwapTableType is AUTO, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case where SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is NOT_STARTED,
        // fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.NOT_STARTED, 100.0, FullVerificationResult.CONSISTENT, false));
    }
}