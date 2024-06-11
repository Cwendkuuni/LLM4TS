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
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.COMPLETED, 90d, FullVerificationResult.CONFLICT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.FAILED, 50d, FullVerificationResult.CONFLICT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FAILED, 50d, FullVerificationResult.CONFLICT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50d, FullVerificationResult.CONFLICT, true));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONFLICT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONFLICT, true));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECKED, false));

        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, false));
        assertNull(SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));
    }
}