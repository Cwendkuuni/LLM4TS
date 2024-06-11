package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.AUTOMATIC;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.COMPLETED, 50d, FullVerificationResult.CONFLICT, false));
        SwapTableType swapTableType1 = SwapTableType.MANUAL;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType1, TaskStatus.FAILED, 50d, FullVerificationResult.CONFLICT, true));
        SwapTableType swapTableType2 = SwapTableType.AUTOMATIC;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType2, TaskStatus.RUNNING, 50d, FullVerificationResult.CONFLICT, false));
        SwapTableType swapTableType3 = SwapTableType.MANUAL;
        SwapTableType swapTableType4 = SwapTableType.MANUAL;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType3, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType4, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, false));
        SwapTableType swapTableType5 = SwapTableType.MANUAL;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType5, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, true));
        SwapTableType swapTableType6 = SwapTableType.MANUAL;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType6, TaskStatus.RUNNING, 100d, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnableWithNullValues() {
        assertNull(SwapTableUtil.isSwapTableEnable(null, null, null, null, null));
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, null, null, null, null));
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, null, null, null));
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, null, null, null));
        assertNull(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, null, null));
        FullVerificationResult fullVerificationResult = null;
        SwapTableType swapTableType = SwapTableType.MANUAL;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100d, fullVerificationResult, null));
        SwapTableType swapTableType1 = SwapTableType.AUTOMATIC;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType1, TaskStatus.RUNNING, 100d, null, false));
        SwapTableType swapTableType2 = SwapTableType.MANUAL;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType2, TaskStatus.RUNNING, 100d, null, true));
    }

    @Test
    public void testIsSwapTableEnableWithValidValues() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSwapTableEnableWithInvalidSwapTableType() {
        SwapTableType swapTableType = null;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));
    }
}