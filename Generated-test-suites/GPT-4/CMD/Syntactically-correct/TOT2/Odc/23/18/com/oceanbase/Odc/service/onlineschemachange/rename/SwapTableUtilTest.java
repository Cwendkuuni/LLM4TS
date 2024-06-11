package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00,
               FullVerificationResult.CONSISTENT, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.00,
                FullVerificationResult.CONSISTENT, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.00,
                FullVerificationResult.CONSISTENT, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.99,
                FullVerificationResult.CONSISTENT, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00,
                FullVerificationResult.INCONSISTENT, true));

        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00,
                FullVerificationResult.UNCHECK, false));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSwapTableType() {
        SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.00,
                FullVerificationResult.CONSISTENT, false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTaskStatus() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, 100.00,
                FullVerificationResult.CONSISTENT, false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullProgressPercentage() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null,
                FullVerificationResult.CONSISTENT, false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFullVerificationResult() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00,
                null, false);
    }
}
