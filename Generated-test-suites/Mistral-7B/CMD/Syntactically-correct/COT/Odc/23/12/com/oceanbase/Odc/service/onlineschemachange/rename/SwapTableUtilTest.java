package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

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
    public void testIsSwapTableEnable_SwapTableTypeIsManual_ManualSwapTableStartedIsFalse_TaskStatusIsRunning_FullTransferProgressPercentageIs100_FullVerificationResultIsConsistentOrUncheck_ReturnsTrue() {
        SwapTableUtil swapTableUtil = new SwapTableUtil();
        assertTrue(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_SwapTableTypeIsManual_ManualSwapTableStartedIsFalse_TaskStatusIsRunning_FullTransferProgressPercentageIsNot100_ReturnsFalse() {
        SwapTableUtil swapTableUtil = new SwapTableUtil();
        fullTransferProgressPercentage = 99.0;
        assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_SwapTableTypeIsManual_ManualSwapTableStartedIsFalse_TaskStatusIsRunning_FullVerificationResultIsInvalid_ReturnsFalse() {
        SwapTableUtil swapTableUtil = new SwapTableUtil();
        FullVerificationResult fullVerificationResultInvalid = mock(FullVerificationResult.class);
        when(fullVerificationResultInvalid.name()).thenReturn("INVALID");
        assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResultInvalid, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_SwapTableTypeIsManual_ManualSwapTableStartedIsFalse_TaskStatusIsNotRunning_ReturnsFalse() {
        SwapTableUtil swapTableUtil = new SwapTableUtil();
        taskStatus = TaskStatus.COMPLETED;
        assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_SwapTableTypeIsManual_ManualSwapTableStartedIsTrue_ReturnsFalse() {
        SwapTableUtil swapTableUtil = new SwapTableUtil();
        manualSwapTableStarted = true;
        assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_SwapTableTypeIsNotManual_ReturnsFalse() {
        SwapTableType swapTableTypeNotManual = SwapTableType.SEMI_AUTO;
        SwapTableUtil swapTableUtil = new SwapTableUtil();
        assertFalse(swapTableUtil.isSwapTableEnable(swapTableTypeNotManual, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }
}