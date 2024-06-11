package com.oceanbase.odc.service.onlineschemachange.rename.test;

import static org.junit.Assert.*;
import static com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil.*;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        assertTrue(isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        swapTableType = SwapTableType.AUTO;
        assertFalse(isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        manualSwapTableStarted = true;
        assertFalse(isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        fullTransferProgressPercentage = 99.0;
        assertFalse(isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        fullVerificationResult = FullVerificationResult.INCONSISTENT;
        assertFalse(isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        fullVerificationResult = FullVerificationResult.ERROR;
        assertFalse(isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSwapTableEnableWithNullFullVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = null;
        boolean manualSwapTableStarted = false;
        isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSwapTableEnableWithNullSwapTableType() {
        SwapTableType swapTableType = null;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    }

}