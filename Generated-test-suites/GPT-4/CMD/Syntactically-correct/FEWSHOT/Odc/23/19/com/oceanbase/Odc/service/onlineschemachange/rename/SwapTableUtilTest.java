package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

/**
 * Unit tests for SwapTableUtil class.
 */

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableTypeManual = SwapTableType.MANUAL;
        SwapTableType swapTableTypeAuto = SwapTableType.AUTO;
        TaskStatus runningStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage100 = 100.0;
        Double fullTransferProgressPercentage99 = 99.0;
        FullVerificationResult consistentResult = FullVerificationResult.CONSISTENT;
        FullVerificationResult uncheckedResult = FullVerificationResult.UNCHECK;
        FullVerificationResult inconsistentResult = FullVerificationResult.INCONSISTENT;
        
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableTypeManual, runningStatus, 
        fullTransferProgressPercentage100, consistentResult, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableTypeManual, runningStatus,
        fullTransferProgressPercentage99, consistentResult, false));

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableTypeManual, runningStatus, 
        fullTransferProgressPercentage100, uncheckedResult, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableTypeManual, runningStatus,
        fullTransferProgressPercentage100, inconsistentResult, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableTypeAuto, runningStatus,
        fullTransferProgressPercentage100, consistentResult, false));
        
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableTypeManual, runningStatus,
        fullTransferProgressPercentage100, consistentResult, true));
    }
}