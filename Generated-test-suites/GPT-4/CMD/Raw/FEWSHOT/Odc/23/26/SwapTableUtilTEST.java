package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        boolean condition1 = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false);

        assertEquals(true, condition1);

        boolean condition2 = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.PAUSED, 
                100.0,
                FullVerificationResult.CONSISTENT,
                false);

        assertEquals(false, condition2);

        boolean condition3 = SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false);

        assertEquals(false, condition3);

        boolean condition4 = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.UNCHECK,
                false);

        assertEquals(true, condition4);

        boolean condition5 = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.INCONSISTENT,
                false);

        assertEquals(false, condition5);

        boolean condition6 = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                99.9,
                FullVerificationResult.CONSISTENT,
                false);

        assertEquals(false, condition6);

        boolean condition7 = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                true);

        assertEquals(false, condition7);

    }
}