package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Assert;
import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        //Test case when all conditions are met
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false));

        //Test case when manualSwapTableStarted is true
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                true));

        //Test case when SwapTableType isn't MANUAL
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTOMATIC,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.UNCHECK,
                false));

        //Test case when the progress percentage isn't 100
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                50.0,
                FullVerificationResult.CONSISTENT,
                false));

        //Test case when the taskStatus isn't RUNNING
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.WAITING,
                100.0,
                FullVerificationResult.CONSISTANT,
                false));

        //Test case when FullVerificationResult isn't either CONSISTENT or UNCHECK
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.INCONSISTANT,
                false));
    }
}