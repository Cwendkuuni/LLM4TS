package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

 // Test case for method isSwapTableEnable with valid inputs
 @Test
 public void testIsSwapTableEnable_ValidInputs() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;
 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for method isSwapTableEnable with SwapTableType as NONE
 @Test
 public void testIsSwapTableEnable_SwapTableType_NONE() {
 SwapTableType swapTableType = SwapTableType.NONE;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for method isSwapTableEnable with manualSwapTableStarted as true
 @Test
 public void testIsSwapTableEnable_manualSwapTableStarted_true() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = true;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for method isSwapTableEnable with fullTransferProgressPercentage less than 100
 @Test
 public void testIsSwapTableEnable_fullTransferProgressPercentage_lessThan_100() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 50.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case for method isSwapTableEnable with fullVerificationResult as ERROR
 @Test
 public void testIsSwapTableEnable_fullVerificationResult_ERROR() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.ERROR;
 boolean manualSwapTableStarted = false;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }
}