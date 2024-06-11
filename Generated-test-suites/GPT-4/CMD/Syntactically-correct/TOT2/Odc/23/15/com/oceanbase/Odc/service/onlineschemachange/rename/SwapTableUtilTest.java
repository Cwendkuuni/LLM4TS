package com.oceanbase.odc.service.onlineschemachange.rename;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {
    
    @Test
    public void testIsSwapTableEnable() {
        //Normal use case
        boolean enabled = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
        FullVerificationResult.CONSISTENT, false);
        assertTrue(enabled);

        //Edge case
        enabled = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.99,
        FullVerificationResult.CONSISTENT, false);
        assertFalse(enabled);

        //Error scenario
        enabled = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
        FullVerificationResult.INCONSISTENT, false);
        assertFalse(enabled);
    }
}