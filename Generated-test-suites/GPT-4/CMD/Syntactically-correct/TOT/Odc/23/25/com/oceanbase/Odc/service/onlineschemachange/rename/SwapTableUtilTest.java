package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import org.junit.Test;
import org.junit.Assert;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
  
        // test typical use case
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                                        FullVerificationResult.CONSISTENT, false);
        Assert.assertTrue(result);

        // test edge case where progress is not 100
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9, 
                                        FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);

        // test edge case where swap table type is not MANUAL
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, 
                                        FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);

        // test edge case where task status is not RUNNING
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.0, 
                                        FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);

        // test edge case where Full Verification Result is INCONSISTENT
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                                        FullVerificationResult.INCONSISTENT, false);
        Assert.assertFalse(result);

        // test edge case where Full Verification Result is CHECKING
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                                        FullVerificationResult.CHECKING, false);
        Assert.assertFalse(result);

        // test edge case where manual swap table has started already
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                                        FullVerificationResult.UNCHECK, true);
        Assert.assertFalse(result);
    }
}
