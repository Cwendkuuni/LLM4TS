package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void isSwapTableEnableTest() {
        // typical use case
        assertEquals(true, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        assertEquals(true, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));

        // edge case
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.0, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // error scenarios
        assertEquals(false, SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, 100.0, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, null, false));
    }
}
