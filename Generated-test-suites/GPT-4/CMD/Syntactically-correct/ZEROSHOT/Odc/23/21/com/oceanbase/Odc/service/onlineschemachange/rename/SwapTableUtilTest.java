package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() { 
      
        // Test with all conditions satisfied
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
          TaskStatus.RUNNING, 100D, FullVerificationResult.CONSISTENT, false));
        
        // Test with SwapTableType is not MANUAL
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, 
          TaskStatus.RUNNING, 100D, FullVerificationResult.CONSISTENT, false));
          
        // Test with taskStatus is not RUNNING
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
          TaskStatus.PENDING, 100D, FullVerificationResult.CONSISTENT, false));
          
        // Test with fullTransferProgressPercentage is not 100
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
          TaskStatus.RUNNING, 99D, FullVerificationResult.CONSISTENT, false));
          
        // Test with fullVerificationResult is not CONSISTENT or UNCHECK
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
          TaskStatus.RUNNING, 100D, FullVerificationResult.INCONSISTENT, false));
          
        // Test with manualSwapTableStarted is true
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
          TaskStatus.RUNNING, 100D, FullVerificationResult.CONSISTENT, true));
    }
}
