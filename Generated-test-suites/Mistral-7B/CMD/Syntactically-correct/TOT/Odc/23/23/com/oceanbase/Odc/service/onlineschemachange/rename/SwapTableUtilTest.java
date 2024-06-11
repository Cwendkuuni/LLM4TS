package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

 // Test case 1: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100%, and fullVerificationResult is CONSISTENT
 @Test
 public void testIsSwapTableEnable_Case1() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case 2: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING, fullTransferProgressPercentage is 100%, and fullVerificationResult is CONSISTENT
 @Test
 public void testIsSwapTableEnable_Case2() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = true;

 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case 3: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100%, and fullVerificationResult is UNCHECK
 @Test
 public void testIsSwapTableEnable_Case3() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
 boolean manualSwapTableStarted = false;

 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case 4: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100%, and fullVerificationResult is INCONSISTENT
 @Test
 public void testIsSwapTableEnable_Case4() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case 5: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is FAILED, fullTransferProgressPercentage is 100%, and fullVerificationResult is CONSISTENT
 @Test
 public void testIsSwapTableEnable_Case5() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.FAILED;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 // Test case 6: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 50%, and fullVerificationResult is CONSISTENT
 @Test
 public void testIsSwapTableEnable_Case6() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 50.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }
}