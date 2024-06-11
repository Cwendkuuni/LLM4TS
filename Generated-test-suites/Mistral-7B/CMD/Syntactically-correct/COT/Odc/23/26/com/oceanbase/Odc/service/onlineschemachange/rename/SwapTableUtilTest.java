package com.oceanbase.odc.service.onlineschemachange.rename.test;

import static org.junit.Assert.*;

import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        Boolean expectedResult = true;
        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsSwapTableEnable_InvalidSwapTableType() {
        SwapTableType swapTableType = SwapTableType.BACKUP;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        Boolean expectedResult = false;
        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        Boolean manualSwapTableStarted = true;
        Boolean expectedResult = false;
        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsSwapTableEnable_TaskStatusNotRunning() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.FAILED;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        Boolean expectedResult = false;
        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsSwapTableEnable_FullTransferProgressPercentageNot100() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 99.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        Boolean expectedResult = false;
        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsSwapTableEnable_FullVerificationResultNotConsistent() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        Boolean expectedResult = false;
        Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_NullFullVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = null;
        Boolean manualSwapTableStarted = false;
        SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    }
}