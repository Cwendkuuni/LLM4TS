package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import org.junit.Test;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        assertTrue(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.CONSISTENT, 
            false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.AUTOMATIC, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.CONSISTENT, 
            false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.STOPPED, 
            100.0, 
            FullVerificationResult.CONSISTENT, 
            false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            99.0, 
            FullVerificationResult.CONSISTENT, 
            false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.INCONSISTENT, 
            false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.CONSISTENT, 
            true));
    }
}