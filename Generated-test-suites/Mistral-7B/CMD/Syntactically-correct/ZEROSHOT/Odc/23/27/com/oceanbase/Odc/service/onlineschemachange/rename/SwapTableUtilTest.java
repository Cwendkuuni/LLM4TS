package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

 @Test
 public void testIsSwapTableEnable() {
 // Test case 1: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;
 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 2: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, and fullVerificationResult is UNCHECK
 SwapTableType swapTableType2 = SwapTableType.MANUAL;
 FullVerificationResult fullVerificationResult2 = FullVerificationResult.UNCHECK;
 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType2, taskStatus, fullTransferProgressPercentage, fullVerificationResult2, manualSwapTableStarted));

 // Test case 3: SwapTableType is not MANUAL, taskStatus is RUNNING, fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
 SwapTableType swapTableType3 = SwapTableType.AUTOMATIC;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType3, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 4: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING, fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
 SwapTableType swapTableType4 = SwapTableType.MANUAL;
 manualSwapTableStarted = true;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType4, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 5: SwapTableType is MANUAL, taskStatus is NOT RUNNING, fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
 SwapTableType swapTableType5 = SwapTableType.MANUAL;
 TaskStatus taskStatus2 = TaskStatus.COMPLETED;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType5, taskStatus2, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 6: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is less than 100, and fullVerificationResult is UNCHECK
 SwapTableType swapTableType6 = SwapTableType.MANUAL;
 Double fullTransferProgressPercentage2 = 50.0;
 FullVerificationResult fullVerificationResult3 = FullVerificationResult.UNCHECK;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType6, taskStatus, fullTransferProgressPercentage2, fullVerificationResult3, manualSwapTableStarted));
 }
}