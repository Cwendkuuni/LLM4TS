package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_manualSwapTableTypeAndRunningTaskStatus() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        assertFalse("Manual swap table is enabled but it should not be since the task status is not 'COMPLETED' or 'FAILED'",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, null, false));
    }

    @Test
    public void testIsSwapTableEnable_manualSwapTableTypeAndManualSwapTableStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        boolean manualSwapTableStarted = true;
        assertFalse("Manual swap table is enabled but it should not be since manual swap table has already been started",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, null, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_manualSwapTableTypeAndRunningTaskStatusAndPercentageGreaterThanOneHundred() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 101.0;
        assertFalse("Manual swap table is enabled but it should not be since the full transfer progress percentage is greater than one hundred",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, null, false));
    }

    @Test
    public void testIsSwapTableEnable_manualSwapTableTypeAndRunningTaskStatusAndConsistentVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        assertFalse("Manual swap table is enabled but it should not be since the full verification result is 'CONSISTENT'",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false));
    }

    @Test
    public void testIsSwapTableEnable_manualSwapTableTypeAndRunningTaskStatusAndUncheckVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        assertTrue("Manual swap table is not enabled but it should be since the full verification result is 'UNCHECK'",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false));
    }

    @Test
    public void testIsSwapTableEnable_automaticSwapTableTypeAndRunningTaskStatus() {
        SwapTableType swapTableType = SwapTableType.AUTOMATIC;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = null;
        FullVerificationResult fullVerificationResult = null;
        assertNull("Automatic swap table status should be null since the method returns false for automatic table types",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false));
    }
}