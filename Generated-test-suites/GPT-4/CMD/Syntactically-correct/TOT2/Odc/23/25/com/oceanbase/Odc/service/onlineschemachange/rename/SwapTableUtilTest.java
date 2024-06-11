
package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {

        // Typical case: SwapTableType is MANUAL, taskStatus is RUNNING, fullTransferProgressPercentage is 100, fullVerificationResult is CONSISTENT and manualSwapTableStarted is false
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));

        // Edge case: SwapTableType is not MANUAL. In this scenario the method should return false irrespective of other parameters.
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));

        // Edge case: taskStatus is not RUNNING. Even if SwapTableType is MANUAL, the method should return false.
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PENDING, 100d, FullVerificationResult.CONSISTENT, false));

        // Edge case: fullTransferProgressPercentage is less than 100. The method should return false.
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99d, FullVerificationResult.CONSISTENT, false));

        // Edge case: fullVerificationResult is neither CONSISTENT nor UNCHECK. The method should return false.
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.INCONSISTENT, false));

        // Edge case: manualSwapTableStarted is true. Irrespective whether SwapTableType is MANUAL, the method should return false.
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, true));
    }

}
