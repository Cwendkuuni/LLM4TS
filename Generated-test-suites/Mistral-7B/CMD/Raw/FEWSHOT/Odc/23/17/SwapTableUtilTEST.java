package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.COMPLETED;
        Double fullTransferProgressPercentage = 50.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        boolean manualSwapTableStarted = true;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        fullTransferProgressPercentage = 100.0;
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        manualSwapTableStarted = false;
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        fullVerificationResult = FullVerificationResult.CONSISTENT;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        fullVerificationResult = FullVerificationResult.UNCHECK;
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        taskStatus = TaskStatus.FAILED;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        taskStatus = TaskStatus.SUCCESS;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        swapTableType = SwapTableType.AUTOMATIC;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        //Test with valid input
        manualSwapTableStarted = false;
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, manualSwapTableStarted));

        manualSwapTableStarted = false;
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, manualSwapTableStarted));

        manualSwapTableStarted = false;
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, null, manualSwapTableStarted));

        manualSwapTableStarted = false;
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, manualSwapTableStarted));

        manualSwapTableStarted = false;
        ManualSwapTableStarted = false;
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, manualSwapTableStarted));
    }

    @Test
    public void testNullInputs() {
        SwapTableType swapTableType = null;
        TaskStatus taskStatus = null;
        Double fullTransferProgressPercentage = null;
        FullVerificationResult fullVerificationResult = null;
        boolean manualSwapTableStarted = true;

        assertNull(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }
}