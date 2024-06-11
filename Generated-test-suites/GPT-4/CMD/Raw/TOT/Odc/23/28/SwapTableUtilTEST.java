package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {
    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        double fullTransferProgressPercentage = 100.00;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;       
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        Assert.assertTrue(result);

        fullTransferProgressPercentage = 99.99;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        Assert.assertFalse(result);

        swapTableType = SwapTableType.AUTO;
        fullTransferProgressPercentage = 100.00;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        Assert.assertFalse(result);

        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.COMPLETED;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        Assert.assertFalse(result);

        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullVerificationResult = FullVerificationResult.INCONSISTENT;
        result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        Assert.assertFalse(result);
    }
}