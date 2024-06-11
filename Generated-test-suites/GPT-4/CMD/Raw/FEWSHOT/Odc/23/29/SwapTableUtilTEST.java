package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        //Test case where all conditions to enable table swapping are met
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
                                                   TaskStatus.RUNNING,
                                                   100.0,
                                                   FullVerificationResult.CONSISTENT,
                                                   false));

        //Test case where swap table type does not match
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, 
                                                    TaskStatus.RUNNING,
                                                    100.0,
                                                    FullVerificationResult.CONSISTENT,
                                                    false));

        //Test case where task status does not match
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
                                                    TaskStatus.TERMINATED,
                                                    100.0,
                                                    FullVerificationResult.CONSISTENT,
                                                    false));

        //Test case where progress percentage is not 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
                                                    TaskStatus.RUNNING,
                                                    99.5,
                                                    FullVerificationResult.CONSISTANT,
                                                    false));

        //Test case where full verification result is INCONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
                                                    TaskStatus.RUNNING,
                                                    100.0,
                                                    FullVerificationResult.INCONSISTANT,
                                                    false));

        //Test case where manual swap table has already started
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
                                                    TaskStatus.RUNNING,
                                                    100.0,
                                                    FullVerificationResult.CONSISTANT,
                                                    true));
    }
}