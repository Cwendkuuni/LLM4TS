package com.oceanbase.odc.service.onlineschemachange.rename;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        boolean result;

        // Test when all conditions are met
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false);
        Assert.assertEquals(true, result);

        // Test with swapTableType is different from MANUAL
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false);
        Assert.assertEquals(false, result);

        // Test with manualSwapTableStarted is true
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, true);
        Assert.assertEquals(false, result);

        // Test with taskStatus different from RUNNING
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PENDING, 100d, FullVerificationResult.CONSISTENT, false);
        Assert.assertEquals(false, result);

        // Test with fullTransferProgressPercentage less than 100
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.99d, FullVerificationResult.CONSISTENT, false);
        Assert.assertEquals(false, result);

        // Test with fullVerificationResult different from CONSISTENT or UNCHECK
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100d, FullVerificationResult.INCONSISTENT, false);
        Assert.assertEquals(false, result);
    }
}