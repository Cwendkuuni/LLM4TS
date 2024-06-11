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
    public void testIsSwapTableEnable_ManualAndRunning_False() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, null, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualAndRunning_ManualSwapTableStarted_False() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        boolean manualSwapTableStarted = true;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, null, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_ManualAndRunning_FullVerificationResult_Consistent_True() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_ManualAndRunning_FullVerificationResult_Uncheck_True() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_OtherAndRunning_False() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, null, false));
    }

    @Test
    public void testIsSwapTableEnable_OtherAndNotRunning_False() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.FINISHED;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, null, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualAndNotRunning_False() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.FINISHED;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, null, false));
    }

    @Test
    public void testIsSwapTableEnable_NullSwapTableType_Null() {
        assertNull(SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, null, null, false));
    }

    @Test
    public void testIsSwapTableEnable_NullTaskStatus_Null() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        assertNull(SwapTableUtil.isSwapTableEnable(swapTableType, null, null, null, false));
    }

    @Test
    public void testIsSwapTableEnable_NullFullTransferProgressPercentage_Null() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        assertNull(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, fullVerificationResult, false));
    }

    @Test
    public void testIsSwapTableEnable_NullFullVerificationResult_Null() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0d;
        assertNull(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, null, false));
    }
}