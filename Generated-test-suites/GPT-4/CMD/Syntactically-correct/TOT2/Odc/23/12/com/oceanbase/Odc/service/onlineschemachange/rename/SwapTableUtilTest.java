package com.oceanbase.odc.service.onlineschemachange.rename;
/*
 * Unit tests for SwapTableUtil.java
 */
 import org.junit.Assert;
import org.junit.Test;

import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;



public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        // typical use case (Manual, Running, 100, Consistent, false)
        Assert.assertTrue("Expected true for the given conditions",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
        
        // edge case (Manual, Running, 99.99, Consistent, false) transferProgressPercentage is not 100
        fullTransferProgressPercentage = 99.99;
        Assert.assertFalse("Expected false for the given conditions",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
        
		// edge case (Manual, Running, 100, Consistent, true) manualSwapTableStarted is true
        fullTransferProgressPercentage = 100.0;
        manualSwapTableStarted = true;
        Assert.assertFalse("Expected false for the given conditions",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
        
        // edge case (Manual, Not Running, 100, Consistent, false) taskStatus is not RUNNING
        manualSwapTableStarted = false;
        taskStatus = TaskStatus.NOT_RUNNING;
        Assert.assertFalse("Expected false for the given conditions",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
        
        // edge case (Auto, Running, 100, Consistent, false) swapTableType is not MANUAL 
        taskStatus = TaskStatus.RUNNING;
        swapTableType = SwapTableType.AUTO;
        Assert.assertFalse("Expected false for the given conditions",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
        
        // error case (null, Running, 100, Consistent, false) swapTableType is null 
        swapTableType = null;
        Assert.assertFalse("Expected false for the given conditions",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }
}