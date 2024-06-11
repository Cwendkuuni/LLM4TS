package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Case 1: All conditions meet the requirement of isSwapTableEnable
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false));

        // Case 2: SwapTableType is not MANUAL
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false));

        // Case 3: TaskStatus is not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PENDING, 100.0,
                FullVerificationResult.CONSISTENT, false));

        // Case 4: fullTransferProgressPercentage is not 100.0
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.0,
                FullVerificationResult.CONSISTENT, false));

        // Case 5: FullVerificationResult is not CONSISTENT and not UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.INCONSISTENT, false));

        // Case 6: manualSwapTableStarted is true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, true));
    }
}
