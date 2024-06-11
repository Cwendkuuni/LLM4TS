package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

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
    public void testIsSwapTableEnable_MANUAL_RUNNING_lessThan100_CONSISTENT() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_RUNNING_lessThan100_UNCHECK() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50d, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_NOT_RUNNING_100_CONSISTENT() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.SUCCEEDED, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_NOT_RUNNING_100_UNCHECK() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.SUCCEEDED, 100d, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_RUNNING_100_MANUAL_STARTED() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_RUNNING_100_MANUAL_STARTED_CONSISTENT() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_RUNNING_100_MANUAL_STARTED_UNCHECK() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, true));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_RUNNING_lessThan100_MANUAL_STARTED() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50d, FullVerificationResult.UNCHECK, true));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_RUNNING_lessThan100_MANUAL_STARTED_CONSISTENT() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50d, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_NOT_RUNNING_NULL() {
        assertNull(SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_MANUAL_NOT_RUNNING_NULL_FULL_VERIFICATION_RESULT() {
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, null, false));
    }
}