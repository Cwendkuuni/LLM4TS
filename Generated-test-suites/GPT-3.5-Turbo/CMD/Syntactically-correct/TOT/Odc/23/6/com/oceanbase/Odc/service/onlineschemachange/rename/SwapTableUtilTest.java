
package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        assertEquals(true, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Edge cases
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.COMPLETED, 50.0, FullVerificationResult.UNCHECK, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9, FullVerificationResult.CONSISTENT, true));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.1, FullVerificationResult.CONSISTENT, false));
        
        // Error scenarios
        assertEquals(false, SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, 100.0, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, null, false));
    }
}
