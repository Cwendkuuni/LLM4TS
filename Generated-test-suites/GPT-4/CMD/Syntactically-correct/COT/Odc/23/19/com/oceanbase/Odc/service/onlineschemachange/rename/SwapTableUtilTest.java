package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_NullParameters() {
        SwapTableUtil.isSwapTableEnable(null, null, null, null, false);
    }

    @Test
    public void testIsSwapTableEnable_LessTransferProgress() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.0, FullVerificationResult.CONSISTENT, false));
    }
}
