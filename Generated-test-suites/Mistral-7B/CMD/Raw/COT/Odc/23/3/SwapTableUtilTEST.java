package com.oceanbase.odc.service.onlineschemachange.rename.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_Valid() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_InvalidSwapTableType() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableStarted() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnable_TaskStatusNotRunning() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_FullTransferProgressPercentageNot100() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_FullVerificationResultNotConsistent() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.INCONSISTENT, false));
    }
}