
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
        // Test case where all conditions are met
        boolean result1 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
                TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertTrue(result1);

        // Test case where manualSwapTableStarted is true
        boolean result2 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
                TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        assertFalse(result2);

        // Test case where taskStatus is not RUNNING
        boolean result3 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
                TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result3);

        // Test case where fullTransferProgressPercentage is not 100
        boolean result4 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
                TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result4);

        // Test case where fullVerificationResult is not CONSISTENT or UNCHECK
        boolean result5 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
                TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false);
        assertFalse(result5);
    }
}
