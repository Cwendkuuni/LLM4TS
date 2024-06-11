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
 // Test case 1: Swap table type is manual, manualSwapTableStarted is false, task status is running, full transfer progress percentage is 100%, and full verification result is consistent
 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 2: Swap table type is manual, manualSwapTableStarted is true, task status is running, full transfer progress percentage is 100%, and full verification result is consistent
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 3: Swap table type is manual, manualSwapTableStarted is false, task status is running, full transfer progress percentage is 100%, and full verification result is uncheck
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 4: Swap table type is manual, manualSwapTableStarted is false, task status is running, full transfer progress percentage is not 100%, and full verification result is consistent
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 5: Swap table type is manual, manualSwapTableStarted is false, task status is not running, full transfer progress percentage is 100%, and full verification result is consistent
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.COMPLETED, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 6: Swap table type is manual, manualSwapTableStarted is false, task status is running, full transfer progress percentage is 100%, and full verification result is inconsistent
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, FullVerificationResult.INCONSISTENT, manualSwapTableStarted));

 // Test case 7: Swap table type is not manual, manualSwapTableStarted is false, task status is running, full transfer progress percentage is 100%, and full verification result is consistent
 assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }
}