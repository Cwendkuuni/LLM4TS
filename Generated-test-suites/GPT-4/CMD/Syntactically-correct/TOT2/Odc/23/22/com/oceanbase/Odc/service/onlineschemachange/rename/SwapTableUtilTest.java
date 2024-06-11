package com.oceanbase.odc.service.onlineschemachange.rename;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;

import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        
        // typical use case
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                        FullVerificationResult.CONSISTENT, false));

        // edge case 1 - different type of swapping
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, 
                        FullVerificationResult.CONSISTENT, false));

        // edge case 2 - manual swapping already started
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                        FullVerificationResult.CONSISTENT, true));

        // edge case 3 - task status not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PAUSED, 100.0, 
                        FullVerificationResult.CONSISTENT, false));

        // error scenario 1 - transfer not yet 100%
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 95.0, 
                        FullVerificationResult.CONSISTENT, false));

        // error scenario 2 - verification result not consistent or uncheck
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                        FullVerificationResult.INCONSISTENT, false));
    }
}