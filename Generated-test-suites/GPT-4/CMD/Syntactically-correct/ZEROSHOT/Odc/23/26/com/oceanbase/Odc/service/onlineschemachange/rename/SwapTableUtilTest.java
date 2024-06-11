
package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

/**
 * @author TestAuthor
 * @date 2025-12-10
 */
public class SwapTableUtilTest {

    Double fullTransferProgressPercentage;
    boolean manualSwapTableStarted;

    @Before
    public void setUp() {
        fullTransferProgressPercentage = 100.0;
        manualSwapTableStarted = false;
    }

    @Test
    public void testIsSwapTableEnable_WithManual() {
        Assert.assertTrue(
            "Expected result is true", 
            SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                fullTransferProgressPercentage, 
                FullVerificationResult.CONSISTENT,  
                manualSwapTableStarted)
        );
    }

    @Test
    public void testIsSwapTableEnable_WithUnCheck() {
        Assert.assertTrue(
            "Expected result is true", 
            SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                fullTransferProgressPercentage, 
                FullVerificationResult.UNCHECK,  
                manualSwapTableStarted)
        );
    }

    @Test
    public void testIsSwapTableEnable_WhenManualSwapStarted() {
        manualSwapTableStarted = true;
        Assert.assertFalse(
            "Expected result is false", 
            SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                fullTransferProgressPercentage, 
                FullVerificationResult.CONSISTENT,  
                manualSwapTableStarted)
        );
    }

    @Test
    public void testIsSwapTableEnable_WhenTaskNotRunning() {
        Assert.assertFalse(
            "Expected result is false", 
            SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.STOPPED, 
                fullTransferProgressPercentage, 
                FullVerificationResult.CONSISTANT, 
                manualSwapTableStarted)
        );
    }

    @Test
    public void testIsSwapTableEnable_WhenTransferNotCompleted() {
        fullTransferProgressPercentage = 90.0;
        Assert.assertFalse(
            "Expected result is false", 
            SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                fullTransferProgressPercentage, 
                FullVerificationResult.CONSISTANT,  
                manualSwapTableStarted)
        );
    }

    @Test
    public void testIsSwapTableEnable_WithInconsistentVerificationResult() {
        fullTransferProgressPercentage = 90.0;
        Assert.assertFalse(
            "Expected result is false", 
            SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                fullTransferProgressPercentage, 
                FullVerificationResult.INCONSISTENT,  
                manualSwapTableStarted)
        );
    }
}
