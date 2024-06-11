package com.oceanbase.odc.service.onlineschemachange.rename.test;

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
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.SUCCESS;
        Double fullTransferProgressPercentage = 50.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        Boolean expectedResult = false;
        Boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertTrue(expectedResult == result);

        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.UNCHECK;
        expectedResult = true;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertTrue(expectedResult == result);

        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.INCONSISTENT;
        expectedResult = false;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertTrue(expectedResult == result);

        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = null;
        fullVerificationResult = FullVerificationResult.UNCHECK;
        expectedResult = false;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertTrue(expectedResult == result);

        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = null;
        expectedResult = false;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertTrue(expectedResult == result);

        swapTableType = SwapTableType.MANUAL;
        taskStatus = null;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.UNCHECK;
        expectedResult = false;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertTrue(expectedResult == result);

        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTENT;
        expectedResult = true;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false);
        assertTrue(expectedResult == result);

        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTENT;
        expectedResult = true;
        manualSwapTableStarted = true;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertFalse(expectedResult == result);

        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTENT;
        expectedResult = true;
        manualSwapTableStarted = false;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, null, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertNull(result);

        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTENT;
        expectedResult = true;
        manualSwapTableStarted = false;
        result = SwapTableUtil.isSwapTableEnable(null, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertNull(result);
    }

}