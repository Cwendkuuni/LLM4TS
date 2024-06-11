package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

 @Test
 public void testIsSwapTableEnable() {
 SwapTableType swapTableType = SwapTableType.AUTO; // Test with Auto swap table type
 TaskStatus taskStatus = TaskStatus.COMPLETED; // Test with completed task status
 Double fullTransferProgressPercentage = 50.0; // Test with partial transfer progress percentage
 FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT; // Test with inconsistent verification result
 boolean manualSwapTableStarted = false; // Test with manual swap table not started

 assertFalse("Swap table should not be enabled with given inputs", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test with valid inputs
 swapTableType = SwapTableType.MANUAL;
 taskStatus = TaskStatus.RUNNING;
 fullTransferProgressPercentage = 100.0;
 fullVerificationResult = FullVerificationResult.CONSISTENT;
 manualSwapTableStarted = false;

 assertTrue("Swap table should be enabled with given inputs", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test with manual swap table started
 manualSwapTableStarted = true;

 assertFalse("Swap table should not be enabled with given inputs", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test with other task statuses
 taskStatus = TaskStatus.FAILED;
 assertFalse("Swap table should not be enabled with failed task status", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 taskStatus = TaskStatus.SUCCESS;
 assertFalse("Swap table should not be enabled with successful task status", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test with other SwapTableTypes
 swapTableType = SwapTableType.FORCE;
 assertFalse("Swap table should not be enabled with given inputs", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }
}