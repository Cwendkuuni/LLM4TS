package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {
  
    @Test
    public void testIsSwapTableEnable_ConsistentAndRunning() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, false);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_UncheckAndRunning() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.UNCHECK, false);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_Inconsistent() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.INCONSISTENT, false);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_NotRunning() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PENDING, 100.00, FullVerificationResult.CONSISTENT, false);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_PartialProgress() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.00, FullVerificationResult.CONSISTENT, false);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableStarted() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, true);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_SwapTableTypeAuto() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, false);

        Assert.assertFalse(result);
    }
}
