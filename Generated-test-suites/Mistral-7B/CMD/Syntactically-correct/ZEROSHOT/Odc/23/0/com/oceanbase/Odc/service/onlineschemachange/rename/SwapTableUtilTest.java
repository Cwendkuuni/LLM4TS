package com.oceanbase.odc.service.onlineschemachange.rename;

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
        // Test case 1: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test case 2: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
        fullVerificationResult = FullVerificationResult.UNCHECK;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test case 3: SwapTableType is NOT MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        SwapTableType swapTableTypeNotManual = SwapTableType.AUTO;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableTypeNotManual, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test case 4: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
        manualSwapTableStarted = true;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test case 5: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is COMPLETED,
        // fullTransferProgressPercentage is 100, fullVerificationResult is null
        taskStatus = TaskStatus.COMPLETED;
        assertNull(fullVerificationResult);
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test case 6: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is null, fullVerificationResult is CONSISTENT
        fullTransferProgressPercentage = null;
        assertNull(fullTransferProgressPercentage);
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test case 7: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is 100, fullVerificationResult is INCONSISTENT
        fullVerificationResult = FullVerificationResult.INCONSISTENT;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test case 8: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING,
        // fullTransferProgressPercentage is -1, fullVerificationResult is CONSISTENT
        fullTransferProgressPercentage = -1.0;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnableNullArguments() {
        // Test case 1: Check null SwapTableType argument
        SwapTableType swapTableType = null;
        assertNull(swapTableType);
        assertNull(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false));

        // Test case 2: Check null TaskStatus argument
        TaskStatus taskStatus = null;
        assertNull(taskStatus);
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, taskStatus, 100.0,
                FullVerificationResult.CONSISTENT, false));

        // Test case 3: Check null Double argument (fullTransferProgressPercentage)
        Double fullTransferProgressPercentage = null;
        assertNull(fullTransferProgressPercentage);
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, fullTransferProgressPercentage,
                FullVerificationResult.CONSISTENT, false));

        // Test case 4: Check null FullVerificationResult argument
        FullVerificationResult fullVerificationResult = null;
        assertNull(fullVerificationResult);
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, fullVerificationResult,
                false));

        // Test case 5: Check null boolean manualSwapTableStarted argument
        boolean manualSwapTableStarted = null;
        assertNull(manualSwapTableStarted);
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, manualSwapTableStarted));
    }
}