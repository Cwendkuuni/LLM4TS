package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.COMPLETED;
        Double fullTransferProgressPercentage = 80.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse("Expected swap table to be disabled with given parameters",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        swapTableType = SwapTableType.MANUAL;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTENT;
        manualSwapTableStarted = false;

        // Test with all conditions met
        assertTrue("Expected swap table to be enabled with given parameters",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        swapTableType = SwapTableType.MANUAL;
        manualSwapTableStarted = true;

        // Test manualSwapTableStarted is true
        assertFalse("Expected swap table to be disabled with given parameters",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        swapTableType = SwapTableType.AUTO;
        fullVerificationResult = FullVerificationResult.UNCHECK;

        // Test fullVerificationResult is UNCHECK
        assertTrue("Expected swap table to be enabled with given parameters",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        fullVerificationResult = FullVerificationResult.INCONSISTENT;

        // Test fullVerificationResult is INCONSISTENT
        assertFalse("Expected swap table to be disabled with given parameters",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        taskStatus = TaskStatus.FAILED;

        // Test taskStatus is FAILED
        assertFalse("Expected swap table to be disabled with given parameters",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Test taskStatus is NULL
        assertNull("Expected taskStatus to be null", SwapTableUtil.isSwapTableEnable(swapTableType, null, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Test fullTransferProgressPercentage is NULL
        assertNull("Expected fullTransferProgressPercentage to be null",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, fullVerificationResult, manualSwapTableStarted));

        // Test fullVerificationResult is NULL
        assertNull("Expected fullVerificationResult to be null",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, null, manualSwapTableStarted));
    }

    @Test
    public void testNullParameters() {
        assertNull("Expected method to return null when given null SwapTableType",
                SwapTableUtil.isSwapTableEnable(null, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        assertNull("Expected method to return null when given null TaskStatus",
                SwapTableUtil.isSwapTableEnable(swapTableType, null, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        assertNull("Expected method to return null when given null FullVerificationResult",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, null, manualSwapTableStarted));

        assertNull("Expected method to return null when given null manualSwapTableStarted",
                SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, null));
    }
}