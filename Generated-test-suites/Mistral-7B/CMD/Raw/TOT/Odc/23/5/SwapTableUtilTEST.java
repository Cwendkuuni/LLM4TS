package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

 // Test case for method isSwapTableEnable with valid inputs
 @Test
 public void testIsSwapTableEnableValidInputs() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for method isSwapTableEnable with invalid inputs - swapTableType is not MANUAL
 @Test(expected = IllegalArgumentException.class)
 public void testIsSwapTableEnableInvalidSwapTableType() {
 SwapTableType swapTableType = SwapTableType.AUTO;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
 }

 // Test case for method isSwapTableEnable with invalid inputs - manualSwapTableStarted is true
 @Test(expected = IllegalStateException.class)
 public void testIsSwapTableEnableManualSwapTableStarted() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = true;

 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
 }

 // Test case for method isSwapTableEnable with invalid inputs - taskStatus is not RUNNING
 @Test(expected = IllegalArgumentException.class)
 public void testIsSwapTableEnableInvalidTaskStatus() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.COMPLETED;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
 }

 // Test case for method isSwapTableEnable with invalid inputs - fullTransferProgressPercentage is less than 100
 @Test(expected = IllegalArgumentException.class)
 public void testIsSwapTableEnableInvalidFullTransferProgressPercentage() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 50.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
 }

 // Test case for method isSwapTableEnable with invalid inputs - fullVerificationResult is null
 @Test(expected = NullPointerException.class)
 public void testIsSwapTableEnableNullFullVerificationResult() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = null;
 boolean manualSwapTableStarted = false;

 SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
 }

 // Test case for method isSwapTableEnable with FullVerificationResult equals UNCHECKED
 @Test
 public void testIsSwapTableEnableFullVerificationResultUnchecked() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
 boolean manualSwapTableStarted = false;

 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

}