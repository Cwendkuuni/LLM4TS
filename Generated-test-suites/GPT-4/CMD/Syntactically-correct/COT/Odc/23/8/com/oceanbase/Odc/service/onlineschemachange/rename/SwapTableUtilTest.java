package com.oceanbase.odc.service.onlineschemachange.rename;
import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_ValidCase() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_InValidCase() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_NullCase() {
        SwapTableUtil.isSwapTableEnable(null, null, null, null, false);
    }

    @Test
    public void testIsSwapTableEnable_LowerPercentage() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false));
    }
}