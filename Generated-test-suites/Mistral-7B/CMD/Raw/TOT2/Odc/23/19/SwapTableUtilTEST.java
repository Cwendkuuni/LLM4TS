package com.oceanbase.odc.service.onlineschemachange.rename.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

public class SwapTableUtilTest {

 @Test
 public void testIsSwapTableEnable() {
 // Test case 1: Return true
 assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

 // Test case 2: Return false
 assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.SUCCEEDED, 50.0, FullVerificationResult.INCONSISTENT, true));
 }

 @Test
 public void testIsSwapTableEnableEdgeCases() {
 // Test case 3: TaskStatus is not RUNNING
 assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.SUCCEEDED, 100.0, FullVerificationResult.CONSISTENT, false));

 // Test case 4: FullTransferProgressPercentage is not 100
 assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false));

 // Test case 5: FullVerificationResult is not CONSISTENT or UNCHECK
 assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));

 // Test case 6: ManualSwapTableStarted is true
 assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));

 // Test case 7: SwapTableType is not MANUAL
 assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
 }
}