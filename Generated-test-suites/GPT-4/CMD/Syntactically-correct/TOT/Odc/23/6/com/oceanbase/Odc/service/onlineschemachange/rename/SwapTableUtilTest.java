package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // check when swap table type is MANUAL
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        assertEquals(true, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // check when taskStatus is different than RUNNING
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.NOT_STARTED, 100.0, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false));

        // check when fullTransferProgressPercentage is not 100
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.99, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.01, FullVerificationResult.CONSISTENT, false));

        // check when fullVerificationResult is different than CONSISTENT or UNCHECK
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));

        // check when manualSwapTableStarted is true
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
    }
}
