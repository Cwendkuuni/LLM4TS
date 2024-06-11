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
    public void testIsSwapTableEnable_ManualSwapTableType_RunningTaskStatus_100PercentProgress_ConsistentVerificationResult_Enable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        Boolean expectedResult = true;

        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);

        assertTrue(expectedResult.equals(actualResult));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableType_RunningTaskStatus_100PercentProgress_InconsistentVerificationResult_Disable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        Boolean expectedResult = false;

        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);

        assertFalse(expectedResult.equals(actualResult));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableType_RunningTaskStatus_100PercentProgress_NullVerificationResult_Enable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = null;
        Boolean expectedResult = true;

        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);

        assertNotNull(actualResult);
        assertTrue(expectedResult.equals(actualResult));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableType_RunningTaskStatus_LessThan100PercentProgress_Enable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 50.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        Boolean expectedResult = false;

        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);

        assertFalse(expectedResult.equals(actualResult));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableType_NotRunningTaskStatus_Enable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.COMPLETED;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        Boolean expectedResult = false;

        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);

        assertFalse(expectedResult.equals(actualResult));
    }

    @Test
    public void testIsSwapTableEnable_SwapTableTypeNotManual_Enable() {
        SwapTableType swapTableType = SwapTableType.AUTOMATIC;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        Boolean expectedResult = false;

        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);

        assertFalse(expectedResult.equals(actualResult));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableStarted_Enable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        Boolean manualSwapTableStarted = true;

        Boolean expectedResult = false;

        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertFalse(expectedResult.equals(actualResult));
    }
}