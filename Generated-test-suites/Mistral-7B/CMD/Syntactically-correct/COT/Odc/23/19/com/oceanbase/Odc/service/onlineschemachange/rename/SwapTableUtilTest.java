package com.oceanbase.odc.service.onlineschemachange.rename.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;

/**
 * SwapTableUtilTest.java
 */
public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_valid() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertTrue("should be true", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_invalid_swapTableType() {
        SwapTableType swapTableType = SwapTableType.AUTOMATIC;

        assertFalse("should be false", SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_invalid_taskStatus() {
        TaskStatus taskStatus = TaskStatus.COMPLETED;

        SwapTableType swapTableType = SwapTableType.MANUAL;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse("should be false", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_invalid_fullTransferProgressPercentage() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        Double fullTransferProgressPercentage = -1.0;

        assertFalse("should be false", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_invalid_fullVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        boolean manualSwapTableStarted = false;

        FullVerificationResult fullVerificationResult = FullVerificationResult.values()[FullVerificationResult.values().length + 1];

        assertFalse("should be false", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_manualSwapTableStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;

        assertFalse("should be false", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_fullVerificationResult_uncheck() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        boolean manualSwapTableStarted = false;

        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;

        assertFalse("should be false", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_array_of_fullVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        boolean manualSwapTableStarted = false;

        FullVerificationResult[] fullVerificationResultValues = Arrays.copyOf(FullVerificationResult.values(), FullVerificationResult.values().length + 1);
        FullVerificationResult invalidFullVerificationResult = new EnumValueBasedAccess(FullVerificationResult.class, "INVALID") {}.getEnumValue();

        assertFalse("should be false", SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, invalidFullVerificationResult, manualSwapTableStarted));
    }
}