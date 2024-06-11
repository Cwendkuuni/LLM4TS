package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false);

        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnableWithNullValues() {
        boolean result = SwapTableUtil.isSwapTableEnable(null, null, null, null, false);

        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnableWithProgress() {
        for(double i = 0.0; i <= 100.0; i++){
            boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, i,
                FullVerificationResult.CONSISTENT, false);

            if(i<100) {
                assertFalse(result);
            }
            else {
                assertTrue(result);
            }
        }
    }

}