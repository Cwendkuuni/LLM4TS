package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

class SwapTableUtilTest {

 @Test
 void testIsSwapTableEnable_Manual_Running_100_Consistent() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

 boolean expectedResult = true;
 boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);

 assertTrue(actualResult, "isSwapTableEnable method should return true when SwapTableType is MANUAL, taskStatus is RUNNING, fullTransferProgressPercentage is 100.0, and fullVerificationResult is CONSISTENT.");
 }

 @Test
 void testIsSwapTableEnable_Manual_Running_100_Uncheck() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;

 boolean expectedResult = true;
 boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);

 assertTrue(actualResult, "isSwapTableEnable method should return true when SwapTableType is MANUAL, taskStatus is RUNNING, fullTransferProgressPercentage is 100.0, and fullVerificationResult is UNCHECK.");
 }

 @Test
 void testIsSwapTableEnable_Manual_NotRunning_100_Consistent() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.COMPLETED;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

 boolean expectedResult = false;
 boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);

 assertFalse(actualResult, "isSwapTableEnable method should return false when SwapTableType is MANUAL, taskStatus is COMPLETED, fullTransferProgressPercentage is 100.0, and fullVerificationResult is CONSISTENT.");
 }

 @Test
 void testIsSwapTableEnable_Manual_Running_0_Consistent() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 0.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

 boolean expectedResult = false;
 boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);

 assertFalse(actualResult, "isSwapTableEnable method should return false when SwapTableType is MANUAL, taskStatus is RUNNING, fullTransferProgressPercentage is 0.0, and fullVerificationResult is CONSISTENT.");
 }

 @Test
 void testIsSwapTableEnable_Manual_Running_100_Inconsistent() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;

 boolean expectedResult = false;
 boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);

 assertFalse(actualResult, "isSwapTableEnable method should return false when SwapTableType is MANUAL, taskStatus is RUNNING, fullTransferProgressPercentage is 100.0, and fullVerificationResult is INCONSISTENT.");
 }

 @Test
 void testIsSwapTableEnable_Manual_Running_100_NullVerificationResult() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = null;

 assertThrows(NullPointerException.class,
 () -> SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false),
 "isSwapTableEnable method should throw a NullPointerException when fullVerificationResult is null.");
 }
}