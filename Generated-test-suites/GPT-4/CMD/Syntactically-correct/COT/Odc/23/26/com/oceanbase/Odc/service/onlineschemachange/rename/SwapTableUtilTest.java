package com.oceanbase.odc.service.onlineschemachange.rename;
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
            false)
        );
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnableNullArgs() {
        SwapTableUtil.isSwapTableEnable(null, null, null, null, false);
    }

    @Test
    public void testIsSwapTableEnableInvalidProgress() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            99.0,
            FullVerificationResult.CONSISTENT,
            false)
        );
    }

    @Test
    public void testIsSwapTableEnableInvalidTaskStatus() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.STOPPED,
            100.0,
            FullVerificationResult.CONSISTENT,
            false)
        );
    }

    @Test
    public void testIsSwapTableEnableInvalidSwapTableType() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.AUTOMATIC,
            TaskStatus.RUNNING,
            100.0,
            FullVerificationResult.CONSISTENT,
            false)
        );
    }
}