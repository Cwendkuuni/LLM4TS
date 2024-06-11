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
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.COMPLETED;
        Double fullTransferProgressPercentage = 99.5;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse("Expected isSwapTableEnable to return false", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        swapTableType = SwapTableType.AUTO;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTENT;

        assertFalse("Expected isSwapTableEnable to return false since manualSwapTableStarted is false", SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, fullTransferProgressPercentage, fullVerificationResult, true));

        manualSwapTableStarted = true;

        assertFalse("Expected isSwapTableEnable to return false since taskStatus is not RUNNING", SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.FINISHED, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        assertFalse("Expected isSwapTableEnable to return false since fullTransferProgressPercentage is not 100", SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 99.9, fullVerificationResult, manualSwapTableStarted));

        assertFalse("Expected isSwapTableEnable to return false since fullVerificationResult is INCONSISTENT", SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, fullTransferProgressPercentage, FullVerificationResult.INCONSISTENT, manualSwapTableStarted));

        // Positive testing - all conditions met
        SwapTableType swapTableTypePositive = SwapTableType.MANUAL;
        TaskStatus taskStatusPositive = TaskStatus.RUNNING;
        Double fullTransferProgressPercentagePositive = 100.0;
        FullVerificationResult fullVerificationResultPositive = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStartedPositive = false;

        assertTrue("Expected isSwapTableEnable to return true", SwapTableUtil.isSwapTableEnable(swapTableTypePositive, taskStatusPositive, fullTransferProgressPercentagePositive, fullVerificationResultPositive, manualSwapTableStartedPositive));

        // Testing null values
        SwapTableType nullSwapTableType = null;
        assertNull("Expected nullSwapTableType to be null", nullSwapTableType);

        FullVerificationResult nullFullVerificationResult = null;
        assertNull("Expected nullFullVerificationResult to be null", nullFullVerificationResult);

        assertFalse("Expected isSwapTableEnable to return false with null SwapTableType", SwapTableUtil.isSwapTableEnable(nullSwapTableType, taskStatusPositive, fullTransferProgressPercentagePositive, fullVerificationResultPositive, manualSwapTableStartedPositive));
    }
}