
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
        // Swap table should be enabled
        assertTrue(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING,
            100.0,
            FullVerificationResult.CONSISTENT,
            false
        ));

        // Check if swap table will not be enabled if not manual
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.AUTOMATIC, 
            TaskStatus.RUNNING,
            100.0,
            FullVerificationResult.CONSISTENT,
            false
        ));

        // Check if swap table will not be enabled if already started
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING,
            100.0,
            FullVerificationResult.CONSISTENT,
            true
        ));

        // Check if swap table will not be enabled if task is not running
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.STOPPED,
            100.0,
            FullVerificationResult.CONSISTENT,
            false
        ));

        // Check if swap table will not be enabled if progress is not 100
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING,
            99.9,
            FullVerificationResult.CONSISTENT,
            false
        ));

        // Check if swap table will not be enabled if results are inconsistent
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING,
            100.0,
            FullVerificationResult.INCONSISTENT,
            false
        ));
    }
}
