
package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        Double fullTransferProgressPercentage = 100.0;

        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
            fullTransferProgressPercentage, FullVerificationResult.CONSISTENT, false));

        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
            fullTransferProgressPercentage, FullVerificationResult.UNCHECK, false));

        //SwapTableType is not MANUAL
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 
            fullTransferProgressPercentage, FullVerificationResult.UNCHECK, false));

        //manualSwapTableStarted is true
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
            fullTransferProgressPercentage, FullVerificationResult.UNCHECK, true));

        //TaskStatus is not RUNNING
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 
            fullTransferProgressPercentage, FullVerificationResult.UNCHECK, false));

        //fullTransferProgressPercentage is not 100
        Double notFullTransferStatus = 75.0;
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
            notFullTransferStatus, FullVerificationResult.UNCHECK, false));

        //FullVerificationResult is other than CONSISTENT and UNCHECK
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
            fullTransferProgressPercentage, FullVerificationResult.INCONSISTENT, false));
    }
}
