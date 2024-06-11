package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_MANUAL_RUNNING_100_CONSISTENT() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_RUNNING_100_UNCHECK() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_RUNNING_100_CONSISTENT_MANUAL_STARTED() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_RUNNING_100_UNCHECK_MANUAL_STARTED() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, true));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_PAUSED_100_CONSISTENT() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PAUSED, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_PAUSED_100_UNCHECK() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PAUSED, 100d, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_PAUSED_100_CONSISTENT_MANUAL_STARTED() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PAUSED, 100d, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_PAUSED_100_UNCHECK_MANUAL_STARTED() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PAUSED, 100d, FullVerificationResult.UNCHECK, true));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_RUNNING_100_CONSISTENT() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_RUNNING_100_UNCHECK() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_RUNNING_100_CONSISTENT_MANUAL_STARTED() {
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100d, null, true));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_RUNNING_100_UNCHECK_MANUAL_STARTED() {
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100d, null, true));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_PAUSED_100_CONSISTENT() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.PAUSED, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_PAUSED_100_UNCHECK() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.PAUSED, 100d, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_PAUSED_100_CONSISTENT_MANUAL_STARTED() {
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.PAUSED, 100d, null, true));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_PAUSED_100_UNCHECK_MANUAL_STARTED() {
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.PAUSED, 100d, null, true));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_RUNNING_50_CONSISTENT() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 50d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_RUNNING_50_UNCHECK() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 50d, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_RUNNING_50_CONSISTENT_MANUAL_STARTED() {
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 50d, null, true));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_RUNNING_50_UNCHECK_MANUAL_STARTED() {
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 50d, null, true));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_PAUSED_50_CONSISTENT() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.PAUSED, 50d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_PAUSED_50_UNCHECK() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.PAUSED, 50d, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_PAUSED_50_CONSISTENT_MANUAL_STARTED() {
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.PAUSED, 50d, null, true));
    }

    @Test
    public void testIsSwapTableEnable_AUTOMATIC_PAUSED_50_UNCHECK_MANUAL_STARTED() {
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.PAUSED, 50d, null, true));
    }

}