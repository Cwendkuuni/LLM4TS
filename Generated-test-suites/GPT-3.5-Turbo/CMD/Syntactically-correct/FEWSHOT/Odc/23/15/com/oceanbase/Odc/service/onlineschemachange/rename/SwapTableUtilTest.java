package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case where all conditions are true
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false
        );
        assertTrue(result);

        // Test case where swapTableType is not MANUAL
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false
        );
        assertFalse(result);

        // Test case where manualSwapTableStarted is true
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                true
        );
        assertFalse(result);

        // Test case where taskStatus is not RUNNING
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.FINISHED,
                100.0,
                FullVerificationResult.CONSISTENT,
                false
        );
        assertFalse(result);

        // Test case where fullTransferProgressPercentage is not 100
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                99.0,
                FullVerificationResult.CONSISTENT,
                false
        );
        assertFalse(result);

        // Test case where fullVerificationResult is not CONSISTENT or UNCHECK
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.INCONSISTENT,
                false
        );
        assertFalse(result);
    }
}