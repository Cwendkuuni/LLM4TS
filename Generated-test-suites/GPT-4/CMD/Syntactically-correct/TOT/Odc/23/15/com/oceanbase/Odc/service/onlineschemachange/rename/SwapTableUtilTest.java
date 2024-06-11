package com.oceanbase.odc.service.onlineschemachange.rename;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        Double fullTransferProgressPercentage = new Double(100);
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        SwapTableType swapTableType = SwapTableType.MANUAL;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, true));

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false));
    }

    @Test
    public void testSwapTableDisable() {
        Double fullTransferProgressPercentage = new Double(99);
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        SwapTableType swapTableType = SwapTableType.MANUAL;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false));

        fullVerificationResult = FullVerificationResult.MISMATCH;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false));
    }
}