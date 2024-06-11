package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // All conditions meet, it should enable the swap table
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
            100.0, FullVerificationResult.CONSISTENT, false));

        // All conditions meet with uncheck, it should still enable the swap table
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
            100.0, FullVerificationResult.UNCHECK, false));

        // Swap table type is not manual, it should not enable the swap table
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 
            100.0, FullVerificationResult.CONSISTENT, false));

        // Manual swap table has started, it should not enable the swap table
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
            100.0, FullVerificationResult.CONSISTENT, true));

        // Task status is not running, it should not enable the swap table
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 
            100.0, FullVerificationResult.CONSISTENT, false));

        // Full transfer progress percentage is not 100, it should not enable the swap table
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
            90.0, FullVerificationResult.CONSISTENT, false));

        // Full verification result is inconsistent, it should not enable the swap table
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
            100.0, FullVerificationResult.INCONSISTENT, false));
    }
}