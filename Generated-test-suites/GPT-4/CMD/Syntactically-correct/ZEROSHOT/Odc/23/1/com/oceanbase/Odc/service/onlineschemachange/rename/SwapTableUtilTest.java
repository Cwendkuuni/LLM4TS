package com.oceanbase.odc.service.onlineschemachange.rename;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
    
        // Test case when all the parameters meet the requirements for a swap table to be enabled.
        assertTrue("expected to return true",
                SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        
        // Test case when SwapTableType is not MANUAL
        assertFalse("expected to return false",
                SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        
        // Test case when manualSwapTableStarted is true
        assertFalse("expected to return false",
                SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
        
        // Test case when taskStatus is not RUNNING
        assertFalse("expected to return false",
                SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.0, FullVerificationResult.CONSISTENT, false));
        
        // Test case when fullTransferProgressPercentage is not 100
        assertFalse("expected to return false",
                SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.0, FullVerificationResult.CONSISTENT, false));
        
        // Test case when fullVerificationResult is not CONSISTENT or UNCHECK
        assertFalse("expected to return false",
                SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));
    }
}