package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import org.junit.Test;

public class SwapTableUtilTest {

 private SwapTableType swapTableType = SwapTableType.MANUAL;
 private TaskStatus taskStatus = TaskStatus.RUNNING;
 private Double fullTransferProgressPercentage = 100.0;
 private FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 private boolean manualSwapTableStarted = false;

 @Test
 public void testIsSwapTableEnable() {
 // Test case 1: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100.0, FullVerificationResult is CONSISTENT
 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 2: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100.0, FullVerificationResult is UNCHECK
 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, null, manualSwapTableStarted));

 // Test case 3: SwapTableType is not MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100.0, FullVerificationResult is CONSISTENT
 assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 4: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING, fullTransferProgressPercentage is 100.0, FullVerificationResult is CONSISTENT
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, true));

 // Test case 5: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is COMPLETED, fullTransferProgressPercentage is 100.0, FullVerificationResult is CONSISTENT
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.COMPLETED, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 6: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is less than 100.0, FullVerificationResult is CONSISTENT
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, new Double(50.0), fullVerificationResult, manualSwapTableStarted));

 // Test case 7: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100.0, FullVerificationResult is INCONSISTENT
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, FullVerificationResult.INCONSISTENT, manualSwapTableStarted));

 // Test case 8: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100.0, FullVerificationResult is NULL
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, null, manualSwapTableStarted));
 }

}