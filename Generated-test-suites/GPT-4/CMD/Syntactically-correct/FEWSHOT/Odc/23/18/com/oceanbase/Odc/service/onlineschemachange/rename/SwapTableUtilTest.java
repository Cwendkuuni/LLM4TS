package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SwapTableUtilTest {
    @Test
    public void testIsSwapTableEnable() {
        assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.8, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO, 
                TaskStatus.RUNNING, 
                100.8, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.STOPPED, 
                100.8, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                98.8, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.8, 
                FullVerificationResult.INCONSISTENT, 
                false)
        );
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.8, 
                FullVerificationResult.CONSISTENT, 
                true)
        );
    }
}