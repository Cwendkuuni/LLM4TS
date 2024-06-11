package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

 private SwapTableUtil utility;

 @Test
 public void testIsSwapTableEnable_validScenario() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 utility = new SwapTableUtil();

 assertTrue(utility.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_invalidSwapTableType() {
 SwapTableType swapTableType = SwapTableType.AUTO;
 // other valid parameters

 utility = new SwapTableUtil();

 assertFalse(utility.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_manualSwapTableStarted() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 // other valid parameters

 utility = new SwapTableUtil();

 manualSwapTableStarted = true;

 assertFalse(utility.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_taskStatusNotRunning() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 // other valid parameters

 utility = new SwapTableUtil();

 taskStatus = TaskStatus.COMPLETED;

 assertFalse(utility.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_fullTransferProgressPercentageNot100() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 // other valid parameters

 utility = new SwapTableUtil();

 fullTransferProgressPercentage = 50.0;

 assertFalse(utility.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_fullVerificationResultNotConsistent() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 // other valid parameters

 utility = new SwapTableUtil();

 fullVerificationResult = FullVerificationResult.INCONSISTENT;

 assertFalse(utility.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_fullVerificationResultNull() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 // other valid parameters

 utility = new SwapTableUtil();

 fullVerificationResult = null;

 assertNull(utility.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }
}