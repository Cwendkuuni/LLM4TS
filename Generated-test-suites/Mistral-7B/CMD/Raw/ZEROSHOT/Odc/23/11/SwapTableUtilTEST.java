package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

/**
 * SwapTableUtilTest
 *
 * @author yaobin
 */
public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case 1: Manual swap table and not started, running task, 100% transfer progress, and consistent verification result
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Test case 2: Manual swap table and started, running task, 100% transfer progress, and consistent verification result
        manualSwapTableStarted = true;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Test case 3: Manual swap table and not started, running task, 100% transfer progress, and unchecked verification result
        fullVerificationResult = FullVerificationResult.UNCHECK;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Test case 4: Automatic swap table, running task, 100% transfer progress, and consistent verification result
        SwapTableType swapTableTypeAutomatic = SwapTableType.AUTOMATIC;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableTypeAutomatic, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Test case 5: Automatic swap table, running task, 100% transfer progress, and unchecked verification result
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableTypeAutomatic, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Test case 6: Running task, 100% transfer progress, and inconsistent verification result
        FullVerificationResult inconsistentVerificationResult = FullVerificationResult.INCONSISTENT;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableTypeAutomatic, taskStatus, fullTransferProgressPercentage, inconsistentVerificationResult, manualSwapTableStarted));

        // Test case 7: Null task status
        assertNotNull(taskStatus);
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableTypeAutomatic, null, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Test case 8: Null fullTransferProgressPercentage
        Double nullFullTransferProgressPercentage = null;
        assertNotNull(fullTransferProgressPercentage);
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableTypeAutomatic, taskStatus, nullFullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }
}