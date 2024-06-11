package com.oceanbase.odc.service.onlineschemachange.rename.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_valid() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_invalidSwapTableType() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.BACKUP, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_invalidTaskStatus() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FINISHED, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_invalidFullTransferProgressPercentage() {
        Random random = new Random();
        int randomInt = random.nextInt(99);
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, (double) randomInt / 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_invalidFullVerificationResult() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.FAILED, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_manualSwapTableStarted() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        assertFalse(result);
    }
}