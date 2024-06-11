package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SwapTableUtilTest {

    private SwapTableType swapTableType;
    private TaskStatus taskStatus;
    private Double fullTransferProgressPercentage;
    private FullVerificationResult fullVerificationResult;
    private boolean manualSwapTableStarted;

    @Before
    public void setup() {
        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTENT;
        manualSwapTableStarted = false;
    }

    @Test
    public void testIsSwapTableEnableShouldTrue() {
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType,taskStatus,fullTransferProgressPercentage,fullVerificationResult,manualSwapTableStarted);
        assertEquals(true, result);
    }

    @Test
    public void testIsSwapTableEnableShouldFalse() {
        taskStatus = TaskStatus.PENDING;
        fullVerificationResult = FullVerificationResult.INCONSISTENT;
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType,taskStatus,fullTransferProgressPercentage,fullVerificationResult,manualSwapTableStarted);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnableManualStarted() {
        manualSwapTableStarted = true;
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType,taskStatus,fullTransferProgressPercentage,fullVerificationResult,manualSwapTableStarted);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnableTaskStatusNotRunning() {
        taskStatus = TaskStatus.PENDING;
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType,taskStatus,fullTransferProgressPercentage,fullVerificationResult,manualSwapTableStarted);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnableFullVerificationResultInconsistent() {
        fullVerificationResult = FullVerificationResult.INCONSISTENT;
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType,taskStatus,fullTransferProgressPercentage,fullVerificationResult,manualSwapTableStarted);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnablePercentNot100() {
        fullTransferProgressPercentage = 99.9;
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType,taskStatus,fullTransferProgressPercentage,fullVerificationResult,manualSwapTableStarted);
        assertEquals(false, result);
    }
}
