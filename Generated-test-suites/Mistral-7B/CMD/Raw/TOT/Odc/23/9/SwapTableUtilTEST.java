package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

 @Test
 public void testIsSwapTableEnable_ValidScenario() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_ManualSwapTableStarted() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = true;

 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_TaskStatusNotRunning() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.COMPLETED;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_FullVerificationResultInconsistent() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_FullTransferProgressPercentageNot100() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 99.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
 boolean manualSwapTableStarted = false;

 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_FullVerificationResultUnchecked() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
 boolean manualSwapTableStarted = false;

 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }
}