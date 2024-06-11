package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.COMPLETED, 99.0, FullVerificationResult.INCONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 99.0, FullVerificationResult.INCONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 99.0, FullVerificationResult.INCONSISTENT, true));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.INCONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.INCONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, true));

        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, null, false));
    }

    @Test
    public void testIsSwapTableEnableNullInputs() {
        assertNull(SwapTableUtil.isSwapTableEnable(null, null, null, null, false));
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, null, null, null, false));
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, SwapTableType.AUTOMATIC, null, null, false));
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, SwapTableType.AUTOMATIC, 100.0, null, false));
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, SwapTableType.AUTOMATIC, 100.0, FullVerificationResult.CONSISTENT, null));
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, SwapTableType.AUTOMATIC, 100.0, FullVerificationResult.CONSISTENT, true));
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, null, false));
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, SwapTableType.MANUAL, 100.0, FullVerificationResult.CONSISTENT, false));
    }
}