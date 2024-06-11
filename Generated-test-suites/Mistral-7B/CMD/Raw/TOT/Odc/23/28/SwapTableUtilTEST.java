package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
    public void setUp() {
        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTENT;
        manualSwapTableStarted = false;
    }

    @Test
    public void testIsSwapTableEnable_HappyPath() {
        // Given
        SwapTableUtil util = new SwapTableUtil();

        // When
        boolean result = util.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertTrue("Swap table should be enabled in happy path", result);
    }

    @Test
    public void testIsSwapTableEnable_SwapTableTypeIsNotMANUAL() {
        // Given
        SwapTableUtil util = new SwapTableUtil();
        SwapTableType swapTableType = SwapTableType.AUTOMATIC;

        // When
        boolean result = util.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertFalse("Swap table should not be enabled if SwapTableType is not MANUAL", result);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableStarted() {
        // Given
        SwapTableUtil util = new SwapTableUtil();
        manualSwapTableStarted = true;

        // When
        boolean result = util.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertFalse("Swap table should not be enabled if manualSwapTableStarted is true", result);
    }

    @Test
    public void testIsSwapTableEnable_TaskStatusIsNotRUNNING() {
        // Given
        SwapTableUtil util = new SwapTableUtil();
        taskStatus = TaskStatus.FAILED;

        // When
        boolean result = util.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertFalse("Swap table should not be enabled if taskStatus is not RUNNING", result);
    }

    @Test
    public void testIsSwapTableEnable_FullTransferProgressPercentageIsNot100() {
        // Given
        SwapTableUtil util = new SwapTableUtil();
        fullTransferProgressPercentage = 99.0;

        // When
        boolean result = util.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertFalse("Swap table should not be enabled if fullTransferProgressPercentage is not 100", result);
    }

    @Test
    public void testIsSwapTableEnable_FullVerificationResultIsNotCONSISTENT_NotUNCHECK() {
        // Given
        SwapTableUtil util = new SwapTableUtil();
        fullVerificationResult = FullVerificationResult.FAILED;

        // When
        boolean result = util.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertFalse("Swap table should not be enabled if fullVerificationResult is not CONSISTENT and not UNCHECK", result);
    }

    @Test
    public void testIsSwapTableEnable_NullFullVerificationResult() {
        // Given
        SwapTableUtil util = new SwapTableUtil();
        fullVerificationResult = null;

        // When
        boolean result = util.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        // Then
        assertNull("fullVerificationResult should be null when it is passed as null", result);
    }
}