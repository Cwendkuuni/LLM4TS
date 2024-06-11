package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_Manual_Running_100_Consistent() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_Manual_Running_100_Uncheck() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_Manual_Running_Less_Than_100() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_Manual_Running_100_Inconsistent() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.INCONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_Manual_NotRunning() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_Manual_Started() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnable_Automatic_Running_100_Consistent() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_Automatic_Running_100_Uncheck() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_Automatic_Running_Less_Than_100() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 50d, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_Automatic_Running_Inconsistent() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100d, FullVerificationResult.INCONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_Automatic_NotRunning() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.COMPLETED, 100d, FullVerificationResult.CONSISTENT, false));
    }
}