package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        FullVerificationResult consistentResult = FullVerificationResult.CONSISTENT;
        FullVerificationResult uncheckedResult = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;
        
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, consistentResult, manualSwapTableStarted));
        
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, uncheckedResult, manualSwapTableStarted));
        
        // Edge case: manualSwapTableStarted is true
        manualSwapTableStarted = true;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, consistentResult, manualSwapTableStarted));
        
        // Edge case: taskStatus is not RUNNING
        taskStatus = TaskStatus.COMPLETED;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, consistentResult, manualSwapTableStarted));
        
        // Edge case: fullTransferProgressPercentage is not 100
        fullTransferProgressPercentage = 50.0;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, consistentResult, manualSwapTableStarted));
        
        // Edge case: swapTableType is not MANUAL
        swapTableType = SwapTableType.AUTOMATIC;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, consistentResult, manualSwapTableStarted));
    }
}