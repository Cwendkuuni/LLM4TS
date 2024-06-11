package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        // Positive test case
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Negative test case 1: Swap table type is not Manual
        SwapTableType swapTableTypeNegative1 = SwapTableType.AUTOMATIC;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableTypeNegative1, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Negative test case 2: Manual swap table started
        boolean manualSwapTableStartedNegative = true;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStartedNegative));

        // Negative test case 3: Task status is not RUNNING
        TaskStatus taskStatusNegative = TaskStatus.COMPLETED;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatusNegative, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Negative test case 4: Full verification result is not CONSISTENT and not UNCHECK
        FullVerificationResult fullVerificationResultNegative = FullVerificationResult.FAILED;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResultNegative, manualSwapTableStarted));

        // Negative test case 5: Full transfer progress percentage is not 100
        Double fullTransferProgressPercentageNegative = 99.0;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentageNegative, fullVerificationResult, manualSwapTableStarted));

        // Null test case 1: Full verification result is null
        FullVerificationResult fullVerificationResultNull = null;
        assertNull(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResultNull, manualSwapTableStarted));

        // Null test case 2: Swap table type is null
        SwapTableType swapTableTypeNull = null;
        assertNull(SwapTableUtil.isSwapTableEnable(swapTableTypeNull, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }
}