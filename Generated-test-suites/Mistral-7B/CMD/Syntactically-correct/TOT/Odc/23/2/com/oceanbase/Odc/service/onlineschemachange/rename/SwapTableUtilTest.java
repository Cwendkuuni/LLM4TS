package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_ValidScenario() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_InvalidSwapTableType() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        // other valid parameters

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        // other valid parameters

        manualSwapTableStarted = true;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_InvalidTaskStatus() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        // other valid parameters

        taskStatus = TaskStatus.FINISHED;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, 100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_InvalidFullVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        // other valid parameters

        fullVerificationResult = FullVerificationResult.MISMATCH;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100.0, fullVerificationResult, false));
    }

    @Test
    public void testIsSwapTableEnable_LessThanFullTransferProgressPercentage() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        // other valid parameters

        fullTransferProgressPercentage = 95.0;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, fullTransferProgressPercentage, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_NegativeFullTransferProgressPercentage() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        // other valid parameters

        fullTransferProgressPercentage = -1.0;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, fullTransferProgressPercentage, FullVerificationResult.CONSISTENT, false));
    }
}