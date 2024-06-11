
package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest { 

    @Test
    public void testIsSwapTableEnable() {
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            100.0,
            FullVerificationResult.CONSISTENT,
            false
        ));

        //Edge case: fullTransferProgressPercentage is less than 100
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            99.9,
            FullVerificationResult.CONSISTENT,
            false
        ));

       //Edge case: TaskStatus is not Running
       Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
           SwapTableType.MANUAL,
           TaskStatus.PENDING,
           100.0,
           FullVerificationResult.CONSISTENT,
           false
       ));

       //Edge case: SwapTableType is not manual
       Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
           SwapTableType.AUTOMATIC,
           TaskStatus.RUNNING,
           100.0,
           FullVerificationResult.CONSISTENT,
           false
       ));

       //Edge case: manualSwapTableStarted is true
       Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
           SwapTableType.MANUAL,
           TaskStatus.RUNNING,
           100.0,
           FullVerificationResult.CONSISTENT,
           true
       ));

       //Edge case: FullVerificationResult is inconsistent
       Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
           SwapTableType.MANUAL,
           TaskStatus.RUNNING,
           100.0,
           FullVerificationResult.INCONSISTENT,
           false
       ));
    }
}
