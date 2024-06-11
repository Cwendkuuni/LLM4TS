
package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    // Test case for isSwapTableEnable method
    @Test
    public void testIsSwapTableEnable() {

        // normal scenario
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // fullTransferProgressPercentage is less than 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false));

        // Task status is not running
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.INTERRUPTED, 100.0, FullVerificationResult.CONSISTENT, false));

        // manualSwapTableStarted is true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));

        // fullVerificationResult is FRAGMENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.FRAGMENT, false));

        // SwapTableType is AUTO
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
    }
}
