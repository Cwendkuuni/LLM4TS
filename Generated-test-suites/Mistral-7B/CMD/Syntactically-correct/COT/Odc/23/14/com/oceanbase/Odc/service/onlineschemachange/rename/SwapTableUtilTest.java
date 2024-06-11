package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @org.junit.Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        fullVerificationResult = FullVerificationResult.INCONSISTENT;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        swapTableType = SwapTableType.AUTOMATIC;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        manualSwapTableStarted = true;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        fullTransferProgressPercentage = 50.0;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIsSwapTableEnableWithNullFullVerificationResult() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, null, false);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIsSwapTableEnableWithNullTaskStatus() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, 100.0, FullVerificationResult.CONSISTENT, false);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIsSwapTableEnableWithNullSwapTableType() {
        SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
    }
}