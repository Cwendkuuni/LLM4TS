package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0,
                FullVerificationResult.CONSISTENT, 
                false);
                
        Assert.assertEquals(true, result);
    }

    @Test
    public void testNotManualSwapTable() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO, 
                TaskStatus.RUNNING, 
                100.0,
                FullVerificationResult.CONSISTENT, 
                false);
                
        Assert.assertEquals(false, result);
    }

    @Test
    public void testManualSwapTableStarted() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0,
                FullVerificationResult.CONSISTENT, 
                true);
                
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testNotRunningStatus() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.STOPPED, 
                100.0,
                FullVerificationResult.CONSISTENT, 
                false);
                
        Assert.assertEquals(false, result);
    }

    @Test
    public void testIncompleteTransferProgress() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                99.9,
                FullVerificationResult.CONSISTENT, 
                false);
                
        Assert.assertEquals(false, result);
    }

    @Test
    public void testNotConsistentOrUnchecked() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0,
                FullVerificationResult.INCONSISTENT, 
                false);
                
        Assert.assertEquals(false, result);
    }

}
