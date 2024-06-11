package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

public class SwapTableUtilTest {

 private SwapTableType swapTableType;
 private TaskStatus taskStatus;
 private Double fullTransferProgressPercentage;
 private FullVerificationResult fullVerificationResult;
 private boolean manualSwapTableStarted;

 @Test
 public void testIsSwapTableEnable() {
 // Test case 1: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
 swapTableType = SwapTableType.MANUAL;
 taskStatus = TaskStatus.RUNNING;
 fullTransferProgressPercentage = 100.0;
 fullVerificationResult = FullVerificationResult.CONSISTENT;
 manualSwapTableStarted = false;
 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 2: SwapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
 swapTableType = SwapTableType.MANUAL;
 taskStatus = TaskStatus.RUNNING;
 fullTransferProgressPercentage = 100.0;
 fullVerificationResult = FullVerificationResult.UNCHECK;
 manualSwapTableStarted = false;
 assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 3: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
 swapTableType = SwapTableType.MANUAL;
 taskStatus = TaskStatus.RUNNING;
 fullTransferProgressPercentage = 100.0;
 fullVerificationResult = FullVerificationResult.CONSISTENT;
 manualSwapTableStarted = true;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 4: SwapTableType is MANUAL, manualSwapTableStarted is true, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
 swapTableType = SwapTableType.MANUAL;
 taskStatus = TaskStatus.RUNNING;
 fullTransferProgressPercentage = 100.0;
 fullVerificationResult = FullVerificationResult.UNCHECK;
 manualSwapTableStarted = true;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 5: SwapTableType is AUTO, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT
 swapTableType = SwapTableType.AUTO;
 taskStatus = TaskStatus.RUNNING;
 fullTransferProgressPercentage = 100.0;
 fullVerificationResult = FullVerificationResult.CONSISTENT;
 manualSwapTableStarted = false;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Test case 6: SwapTableType is AUTO, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is UNCHECK
 swapTableType = SwapTableType.AUTO;
 taskStatus = TaskStatus.RUNNING;
 fullTransferProgressPercentage = 100.0;
 fullVerificationResult = FullVerificationResult.UNCHECK;
 manualSwapTableStarted = false;
 assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }

}