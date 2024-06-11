package com.oceanbase.odc.service.onlineschemachange.rename;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            Double.valueOf(100.0f),
            FullVerificationResult.CONSISTENT, 
            false)
        );

        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            Double.valueOf(100.0f),
            FullVerificationResult.UNCHECK, 
            false)
        );

        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.FINISHED, 
            Double.valueOf(100.0f),
            FullVerificationResult.CONSISTENT, 
            false)
        );
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnableWithNulls() {
        SwapTableUtil.isSwapTableEnable(
            null, 
            null, 
            null, 
            null, 
            false
        );
    }
}