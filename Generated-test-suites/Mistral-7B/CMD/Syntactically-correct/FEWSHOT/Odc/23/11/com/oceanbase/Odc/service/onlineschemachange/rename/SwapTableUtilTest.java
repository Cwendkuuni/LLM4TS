package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Scenario 1: All conditions are true
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Scenario 2: SwapTableType is not MANUAL
        SwapTableType swapTableType1 = SwapTableType.AUTO;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType1, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Scenario 3: ManualSwapTableStarted is true
        boolean manualSwapTableStarted1 = true;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted1));

        // Scenario 4: TaskStatus is not RUNNING
        TaskStatus taskStatus1 = TaskStatus.COMPLETED;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus1, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Scenario 5: FullTransferProgressPercentage is not 100
        Double fullTransferProgressPercentage1 = 99.0;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage1, fullVerificationResult, manualSwapTableStarted));

        // Scenario 6: FullVerificationResult is not CONSISTENT and not UNCHECK
        FullVerificationResult fullVerificationResult1 = FullVerificationResult.FAILED;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));

        // Scenario 7: Inputs are null
        SwapTableType swapTableType2 = null;
        assertNull(SwapTableUtil.isSwapTableEnable(swapTableType2, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
        TaskStatus taskStatus2 = null;
        assertNull(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus2, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
        Double fullTransferProgressPercentage2 = null;
        assertNull(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage2, fullVerificationResult, manualSwapTableStarted));
        FullVerificationResult fullVerificationResult2 = null;
        assertNull(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult2, manualSwapTableStarted));
        boolean manualSwapTableStarted2 = null;
        assertNull(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted2));
    }
}