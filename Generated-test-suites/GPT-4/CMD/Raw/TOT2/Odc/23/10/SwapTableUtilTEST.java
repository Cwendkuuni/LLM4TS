package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    // Test Case for Positive Scenario
    @Test
    public void testIsSwapTableEnable() {
        boolean result = 
            SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100d, 
                FullVerificationResult.CONSISTENT,
                false);
        assertEquals(result, true);
    }

    // Test Case for Edge Case
    @Test
    public void testIsSwapTableEnableEdgeCase() {
        boolean result = 
            SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                99.99d,
                FullVerificationResult.CONSISTENT,
                false);
        assertEquals(result, false);
    }

    // Test Case for Error Scenario
    @Test
    public void testIsSwapTableEnableErrorCase() {
        boolean result = 
            SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTOMATIC,
                TaskStatus.RUNNING,
                100d, 
                FullVerificationResult.CONSISTENT,
                false);
        assertEquals(result, false);
    }
}