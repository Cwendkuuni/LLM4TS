package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {
    
    @Test
    public void testIsSwapTableEnable() {
        // All conditions are satisfied, expect to return true
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.CONSISTENT, false));
            
        // Full Transfer Progress Percentage is less than 100%, expect to return false
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 80.0,
            FullVerificationResult.CONSISTENT, false));

        // Full Verification Result is InConsistent, expect to return false
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.INCONSISTENT, false));

        // Swap Table type is not MANUAL, expect to return false
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.CONSISTENT, false));

        // Task status is not RUNNING, expect to return false
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FAILED, 100.0,
            FullVerificationResult.CONSISTENT, false));
            
        // Manual Swap Table already started, expect to return false
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
            FullVerificationResult.CONSISTENT, true));
    }
}