package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // All conditions meet for swap table to be enabled
        assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false));

        // Test case where swapTableType is not MANUAL
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false));

        // Test case where manualSwapTableStarted is true
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                true));

        // Test case where taskStatus is not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.FAILED,
                100.0,
                FullVerificationResult.CONSISTENT,
                false));

        // Test case where fullTransferProgressPercentage is less than 100
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                99.5,
                FullVerificationResult.CONSISTENT,
                false));

        // Test case where fullVerificationResult is not CONSISTENT or UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.INCONSISTENT,
                false));
    }
}
