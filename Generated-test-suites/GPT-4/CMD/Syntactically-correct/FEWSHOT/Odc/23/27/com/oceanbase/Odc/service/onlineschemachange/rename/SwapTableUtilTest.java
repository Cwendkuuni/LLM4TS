package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SwapTableUtilTest {
    @Test
    public void testIsSwapTableEnable() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));
        
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.0, FullVerificationResult.CONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.0, FullVerificationResult.CONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
    }
}