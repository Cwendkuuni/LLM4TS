package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        fullTransferProgressPercentage = 100.0D;
        fullVerificationResult = FullVerificationResult.CONSISTENT;
        manualSwapTableStarted = false;
    }

    @Test
    public void testIsSwapTableEnable_Normal_Scenario() {
        // Given

        // When
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertTrue("Swap table should be enabled in normal scenario", result);
    }

    @Test
    public void testIsSwapTableEnable_SwapTableType_IS_NOT_MANUAL() {
        // Given
        SwapTableType swapTableType = SwapTableType.AUTOMATIC;

        // When
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertFalse("Swap table should not be enabled when SwapTableType is not manual", result);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableStarted_IS_TRUE() {
        // Given
        manualSwapTableStarted = true;

        // When
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertFalse("Swap table should not be enabled when manualSwapTableStarted is true", result);
    }

    @Test
    public void testIsSwapTableEnable_TaskStatus_IS_NOT_RUNNING() {
        // Given
        taskStatus = TaskStatus.COMPLETED;

        // When
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertFalse("Swap table should not be enabled when taskStatus is not running", result);
    }

    @Test
    public void testIsSwapTableEnable_FullVerificationResult_IS_NOT_CONSISTENT_AND_NOT_UNCHECK() {
        // Given
        fullVerificationResult = FullVerificationResult.FAILED;

        // When
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertFalse("Swap table should not be enabled when fullVerificationResult is not consistent and not unchecked", result);
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_FullVerificationResult_IS_NULL() {
        // Given
        fullVerificationResult = null;

        // When
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        // An exception should be thrown
    }
}