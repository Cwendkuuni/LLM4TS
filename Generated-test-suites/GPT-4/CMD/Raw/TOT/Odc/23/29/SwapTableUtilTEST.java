
package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {

        //Let's get a positive scenario when all conditions are met
        assertTrue(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL, 
                        TaskStatus.RUNNING, 
                        100.0, 
                        FullVerificationResult.CONSISTENT, 
                        false));

        //Let's test when swapTableType is not Manual
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.AUTOMATIC, 
                        TaskStatus.RUNNING, 
                        100.0, 
                        FullVerificationResult.CONSISTENT, 
                        false));

        //Let's test when taskStatus is not running
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL, 
                        TaskStatus.FAILED, 
                        100.0, 
                        FullVerificationResult.CONSISTANT, 
                        false));

        //Let's test when fullTransferProgressPercentage is not 100.0
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL, 
                        TaskStatus.RUNNING, 
                        80.0, 
                        FullVerificationResult.CONSISTENT, 
                        false));

        //Let's test when fullVerificationResult is not CONSISTENT or UNCHECK
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL, 
                        TaskStatus.RUNNING, 
                        100.0, 
                        FullVerificationResult.INCONSISTENT, 
                        false));

        //Let's test when manualSwapTableStarted is already started
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL, 
                        TaskStatus.RUNNING, 
                        100.0, 
                        FullVerificationResult.CONSISTANT, 
                        true));
    }
}
