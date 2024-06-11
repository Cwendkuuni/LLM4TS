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
 // Test case 1: SwapTableType is not MANUAL and manualSwapTableStarted is false
 SwapTableType swapTableType = SwapTableType.PROACTIVE;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 50.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
 boolean manualSwapTableStarted = false;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 2: SwapTableType is MANUAL, manualSwapTableStarted is false, and taskStatus is not RUNNING
 swapTableType = SwapTableType.MANUAL;
 taskStatus = TaskStatus.COMPLETED;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 3: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, and fullTransferProgressPercentage is not 100
 fullTransferProgressPercentage = 80.0;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 4: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, and fullVerificationResult is INCONSISTENT
 fullTransferProgressPercentage = 100.0;
 fullVerificationResult = FullVerificationResult.INCONSISTENT;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 5: All conditions are met
 swapTableType = SwapTableType.MANUAL;
 manualSwapTableStarted = false;
 taskStatus = TaskStatus.RUNNING;
 fullTransferProgressPercentage = 100.0;
 fullVerificationResult = FullVerificationResult.CONSISTENT;
 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

}