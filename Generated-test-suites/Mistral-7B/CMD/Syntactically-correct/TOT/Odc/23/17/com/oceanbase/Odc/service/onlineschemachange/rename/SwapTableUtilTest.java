package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

 // Test case for isSwapTableEnable method with valid input and expected output (true)
 @Test
 public void testIsSwapTableEnable_ValidInput_ExpectedOutput() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertTrue("SwapTableUtil.isSwapTableEnable() should return true for given input",
 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for isSwapTableEnable method with invalid input (swapTableType as null) and expected output (false)
 @Test
 public void testIsSwapTableEnable_InvalidInput_ExpectedOutput() {
 SwapTableType swapTableType = null;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse("SwapTableUtil.isSwapTableEnable() should return false for null swapTableType",
 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for isSwapTableEnable method with invalid input (taskStatus as null) and expected output (false)
 @Test
 public void testIsSwapTableEnable_InvalidInput_TaskStatus_ExpectedOutput() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = null;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse("SwapTableUtil.isSwapTableEnable() should return false for null taskStatus",
 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for isSwapTableEnable method with invalid input (fullTransferProgressPercentage as negative) and expected output (false)
 @Test
 public void testIsSwapTableEnable_InvalidInput_NegativeFullTransferProgressPercentage_ExpectedOutput() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = -1.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse("SwapTableUtil.isSwapTableEnable() should return false for negative fullTransferProgressPercentage",
 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for isSwapTableEnable method with invalid input (fullVerificationResult as null) and expected output (false)
 @Test
 public void testIsSwapTableEnable_InvalidInput_NullFullVerificationResult_ExpectedOutput() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = null;
 boolean manualSwapTableStarted = false;

 assertFalse("SwapTableUtil.isSwapTableEnable() should return false for null fullVerificationResult",
 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for isSwapTableEnable method with invalid input (manualSwapTableStarted as true) and expected output (false)
 @Test
 public void testIsSwapTableEnable_InvalidInput_ManualSwapTableStarted_ExpectedOutput() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = true;

 assertFalse("SwapTableUtil.isSwapTableEnable() should return false for manualSwapTableStarted = true",
 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for isSwapTableEnable method with invalid input (swapTableType as INVALID) and expected output (false)
 @Test
 public void testIsSwapTableEnable_InvalidInput_InvalidSwapTableType_ExpectedOutput() {
 SwapTableType swapTableType = SwapTableType.INVALID;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse("SwapTableUtil.isSwapTableEnable() should return false for invalid swapTableType",
 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for isSwapTableEnable method with invalid input (taskStatus as COMPLETED) and expected output (false)
 @Test
 public void testIsSwapTableEnable_InvalidInput_CompletedTaskStatus_ExpectedOutput() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.COMPLETED;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse("SwapTableUtil.isSwapTableEnable() should return false for completed taskStatus",
 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for isSwapTableEnable method with invalid input (fullVerificationResult as FAILED) and expected output (false)
 @Test
 public void testIsSwapTableEnable_InvalidInput_FailedFullVerificationResult_ExpectedOutput() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.FAILED;
 boolean manualSwapTableStarted = false;

 assertFalse("SwapTableUtil.isSwapTableEnable() should return false for failed fullVerificationResult",
 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }
}