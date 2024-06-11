package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {

    // Test Case 1: When swap table is not manual type
    @Test
    public void isSwapTableEnable_NotManualType_ReturnFalse() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);
    }

    // Test Case 2: When swap table is manual type but task status is not running
    @Test
    public void isSwapTableEnable_ManualTypeButNotRuningTaskStatus_ReturnFalse() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.SUSPENDED, 100.0, FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);
    }

    // Test Case 3: When swap table is manual type, task status is running but full transfer progress percentage is not 100
    @Test
    public void isSwapTableEnable_ManualTypeButNotFullProgressPercentage_ReturnsFalse() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9, FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);
    }

    // Test Case 4: When swap table is manual type, task status is running, transfer progress is 100% but full verification result is not either consistent or unchecked
    @Test
    public void isSwapTableEnable_ManualTypeButNotConsistentFullVerificationResult_ReturnsFalse() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false);
        Assert.assertFalse(result);
    }

    // Test Case 5: All conditions are met for swap table operation to be enabled
    @Test
    public void isSwapTableEnable_AllConditionsMet_ReturnsTrue() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
        Assert.assertTrue(result);
    }

}

