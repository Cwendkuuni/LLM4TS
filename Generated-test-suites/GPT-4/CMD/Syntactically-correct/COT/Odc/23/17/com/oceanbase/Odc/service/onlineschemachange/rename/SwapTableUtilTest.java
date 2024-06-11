package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

public class SwapTableUtilTest {
    @Test
    public void testIsSwapTableEnabled() {
        //Basic functionality test
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9, FullVerificationResult.CONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.0, FullVerificationResult.CONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        //Edge cases and exceptions
        try {
            SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException ignored) {
        }

        try {
            SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, 100.0, FullVerificationResult.CONSISTENT, false);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException ignored) {
        }

        try {
            SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, FullVerificationResult.CONSISTENT, false);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException ignored) {
        }

        try {
            SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, null, false);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException ignored) {
        }
    }
}
