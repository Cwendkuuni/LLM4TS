package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

/**
 * SwapTableUtilTest
 */
public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_GivenManualSwapTableType_False_WhenManualSwapTableStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        boolean manualSwapTableStarted = true;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100D, null, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_GivenManualSwapTableType_True_WhenManualSwapTableNotStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        boolean manualSwapTableStarted = false;

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100D, FullVerificationResult.CONSISTENT, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_GivenManualSwapTableType_False_WhenTaskStatusIsNotRunning() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        boolean manualSwapTableStarted = false;
        TaskStatus taskStatus = TaskStatus.COMPLETED;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, 100D, FullVerificationResult.CONSISTENT, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_GivenManualSwapTableType_False_WhenFullTransferProgressPercentageNot100() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        boolean manualSwapTableStarted = false;
        Double fullTransferProgressPercentage = 99D;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, fullTransferProgressPercentage, null, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_GivenManualSwapTableType_False_WhenFullVerificationResultIsNotConsistentAndUncheck() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        boolean manualSwapTableStarted = false;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100D, fullVerificationResult, manualSwapTableStarted));
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100D, FullVerificationResult.FAILED, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_GivenManualSwapTableType_True_WhenTaskStatusIsRunningAndFullVerificationResultIsUnchecked() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        boolean manualSwapTableStarted = false;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, 100D, fullVerificationResult, manualSwapTableStarted));
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_GivenNullFullVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        boolean manualSwapTableStarted = false;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100D;

        SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, null, manualSwapTableStarted);
    }
}