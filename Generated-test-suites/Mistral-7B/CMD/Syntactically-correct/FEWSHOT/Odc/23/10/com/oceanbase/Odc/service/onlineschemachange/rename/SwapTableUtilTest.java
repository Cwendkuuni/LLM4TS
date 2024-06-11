package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
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
    public void testIsSwapTableEnable_ManualSwapTableType_NotManualSwapTableStarted_RunningTaskStatus_100PercentProgress_ConsistentFullVerificationResult_ReturnsTrue() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableType_ManualSwapTableStarted_RunningTaskStatus_100PercentProgress_ConsistentFullVerificationResult_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableType_NotManualSwapTableStarted_RunningTaskStatus_100PercentProgress_UncheckFullVerificationResult_ReturnsTrue() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableType_ManualSwapTableStarted_RunningTaskStatus_100PercentProgress_UncheckFullVerificationResult_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = true;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableType_NotManualSwapTableStarted_NotRunningTaskStatus_100PercentProgress_ConsistentFullVerificationResult_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.COMPLETED;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableType_NotManualSwapTableStarted_NotRunningTaskStatus_100PercentProgress_UncheckFullVerificationResult_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.COMPLETED;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_NotManualSwapTableType_AnyStatus_AnyProgress_AnyFullVerificationResult_AnyManualSwapTableStarted_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.AUTOMATIC;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 50.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_NullSwapTableType_AnyStatus_AnyProgress_AnyFullVerificationResult_AnyManualSwapTableStarted_ThrowsNullPointerException() {
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 50.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;

        SwapTableType swapTableType = null;

        SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    }
}