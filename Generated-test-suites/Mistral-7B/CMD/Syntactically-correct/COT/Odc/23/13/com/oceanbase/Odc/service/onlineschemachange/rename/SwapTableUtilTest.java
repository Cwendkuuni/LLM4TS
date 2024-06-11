package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

/**
 * SwapTableUtilTest
 */
public class SwapTableUtilTest {

    /**
     * Test method for {@link SwapTableUtil#isSwapTableEnable(SwapTableType, TaskStatus, Double, FullVerificationResult, boolean)}
     */
    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Test with different input values and combinations
        SwapTableType swapTableType1 = SwapTableType.AUTOMATIC;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType1, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        SwapTableType swapTableType2 = SwapTableType.MANUAL;
        manualSwapTableStarted = true;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType2, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        TaskStatus taskStatus1 = TaskStatus.COMPLETED;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus1, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        SwapTableType swapTableType3 = SwapTableType.AUTOMATIC;
        Double fullTransferProgressPercentage1 = 50.0;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType3, taskStatus, fullTransferProgressPercentage1, fullVerificationResult, manualSwapTableStarted));

        FullVerificationResult fullVerificationResult1 = FullVerificationResult.INCONSISTENT;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));
    }
}