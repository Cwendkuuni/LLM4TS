package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

 // Test case 1: Check if table is enabled when all conditions are met (valid scenario)
 @Test
 public void testIsSwapTableEnable_ValidScenario() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 Boolean expectedResult = true;
 Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
 assertTrue("Table is enabled when all conditions are met", expectedResult == actualResult);
 }

 // Test case 2: Check if table is not enabled when swap table type is not manual
 @Test
 public void testIsSwapTableEnable_SwapTableTypeNotManual() {
 SwapTableType swapTableType = SwapTableType.AUTOMATIC;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 Boolean expectedResult = false;
 Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
 assertFalse("Table is not enabled when swap table type is not manual", expectedResult == actualResult);
 }

 // Test case 3: Check if table is not enabled when a manual swap table start has been initiated
 @Test
 public void testIsSwapTableEnable_ManualSwapTableStarted() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 Boolean expectedResult = false;
 Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, true);
 assertFalse("Table is not enabled when a manual swap table start has been initiated", expectedResult == actualResult);
 }

 // Test case 4: Check if table is enabled when task status is not running
 @Test
 public void testIsSwapTableEnable_TaskStatusNotRunning() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.COMPLETED;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 Boolean expectedResult = false;
 Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
 assertFalse("Table is not enabled when task status is not running", expectedResult == actualResult);
 }

 // Test case 5: Check if table is enabled when full verification result is not consistent
 @Test
 public void testIsSwapTableEnable_FullVerificationResultNotConsistent() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.INVALID;
 Boolean expectedResult = false;
 Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
 assertFalse("Table is not enabled when full verification result is not consistent", expectedResult == actualResult);
 }

 // Test case 6: Check if table is enabled when full transfer progress percentage is not 100
 @Test
 public void testIsSwapTableEnable_FullTransferProgressPercentageNot100() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 50.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 Boolean expectedResult = false;
 Boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
 assertFalse("Table is not enabled when full transfer progress percentage is not 100", expectedResult == actualResult);
 }
}