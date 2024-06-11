package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {
    @Test
    public void testIsSwapTableEnableWithAllConditionsMet() {
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnableWithNonManualSwapTable() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.ANY_OTHER_TYPE, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnableWithNotRunningTask() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.ANY_OTHER_STATUS, 100.0,
            FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnableWithLessTransferProgress() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9,
            FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnableWithInconsistentVerificationResult() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.INCONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnableWithStartedManualSwapTable() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.CONSISTENT, true));
    }
}