
package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static com.oceanbase.odc.core.shared.constant.TaskStatus.RUNNING;
import static com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult.CONSISTENT;
import static com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult.UNCHECK;
import static com.oceanbase.odc.service.onlineschemachange.model.SwapTableType.MANUAL;


public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        assertEquals(true, SwapTableUtil.isSwapTableEnable(MANUAL, RUNNING, 100.0, CONSISTENT, false));
        assertEquals(true, SwapTableUtil.isSwapTableEnable(MANUAL, RUNNING, 100.0, UNCHECK, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(MANUAL, RUNNING, 99.99, CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(MANUAL, RUNNING, 100.0, CONSISTENT, true));
    }
}
