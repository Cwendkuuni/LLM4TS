package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTANT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTANT, true));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
            TaskStatus.PAUSED, 100.0, FullVerificationResult.UNCHECK, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, 
            TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));
    }            
}