package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {
    
    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResultConsistent = FullVerificationResult.CONSISTENT;
        FullVerificationResult fullVerificationResultUncheck = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;

        // Testing with all conditions met
        assertEquals(true, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResultConsistent, manualSwapTableStarted));

        // Testing with manualSwapTableStarted set to true
        manualSwapTableStarted = true;
        assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResultConsistent, manualSwapTableStarted));

        // Testing with taskStatus not set to RUNNING
        taskStatus = TaskStatus.FINISHED;
        manualSwapTableStarted = false;
        assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResultConsistent, manualSwapTableStarted));

        // Testing with fullTransferProgressPercentage not equal to 100
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 50.0;
        assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResultConsistent, manualSwapTableStarted));

        // Testing with fullVerificationResult not set to CONSISTENT or UNCHECK
        fullTransferProgressPercentage = 100.0;
        assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, FullVerificationResult.INVALID, manualSwapTableStarted));
    }
}