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
        FullVerificationResult fullVerificationResult1 = FullVerificationResult.CONSISTENT;
        FullVerificationResult fullVerificationResult2 = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;
        
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));
        
        manualSwapTableStarted = true;  
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));
        
        swapTableType = SwapTableType.AUTO;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));
        
        taskStatus = TaskStatus.PENDING;  
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));
        
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 50.0;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));
        
        fullTransferProgressPercentage = 100.0;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult2, manualSwapTableStarted));
        
        fullVerificationResult2 = FullVerificationResult.CONSISTENT;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult2, manualSwapTableStarted));
    }
}