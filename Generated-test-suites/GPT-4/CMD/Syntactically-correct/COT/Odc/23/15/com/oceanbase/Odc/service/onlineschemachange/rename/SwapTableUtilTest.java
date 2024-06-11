package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {
    @Test
    public void testIsSwapTableEnable() {
        assertTrue(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            100.00,
            FullVerificationResult.CONSISTENT,
            false)
        );
    }

    @Test
    public void testEdgeCaseSwapTableEnable() {
        // add edge case tests here

        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.AUTO,
            TaskStatus.RUNNING,
            100.00,
            FullVerificationResult.CONSISTENT,
            false)
        );

        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.COMPLETED,
            100.00,
            FullVerificationResult.CONSISTENT,
            false)
        );

        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            90.00,
            FullVerificationResult.CONSISTENT,
            false)
        );

        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            100.00,
            FullVerificationResult.INCONSISTENT,
            false)
        );

        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            100.00,
            FullVerificationResult.CONSISTENT,
            true)
        );

         assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            null,
            FullVerificationResult.CONSISTENT,
            false)
        );
    }
}