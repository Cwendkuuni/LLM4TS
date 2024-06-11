package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult1 = FullVerificationResult.CONSISTENT;
        FullVerificationResult fullVerificationResult2 = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;

        // Testing with all conditions met
        assertEquals(true, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));
        assertEquals(true, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult2, manualSwapTableStarted));

        // Testing with different SwapTableType
        swapTableType = SwapTableType.AUTO;
        assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));

        // Testing with manual swap table being started
        swapTableType = SwapTableType.MANUAL;
        manualSwapTableStarted = true;
        assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));

        // Testing with TaskStatus not RUNNING
        taskStatus = TaskStatus.COMPLETED;
        manualSwapTableStarted = false;
        assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));

        // Testing with full transfer progress percentage not equal to 100
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 99.9;
        assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult1, manualSwapTableStarted));
    }
}