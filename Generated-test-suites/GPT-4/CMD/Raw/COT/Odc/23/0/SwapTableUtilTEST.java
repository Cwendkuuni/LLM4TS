package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {
    
    @Test
    public void testIsSwapTableEnable() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false ));
    }

    @Test
    public void testIsSwapTableEnableWhenProgressIsNull() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, FullVerificationResult.CONSISTENT, false ));
    }

    @Test
    public void testIsSwapTableEnableWhenProgressIsLessThan100() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false ));
    }

    @Test
    public void testIsSwapTableEnableWhenProgressIsMoreThan100() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.01, FullVerificationResult.CONSISTENT, false ));
    }

    @Test
    public void testIsSwapTableEnableWhenSwapTableTypeIsNonManual() {
        // assuming AUTO is another valid SwapTableType
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false ));
    }

    @Test
    public void testIsSwapTableEnableWhenFullVerificationResultIsInconsistent() {
        // Assuming INCONSISTENT is another valid FullVerificationResult
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false ));
    }

    @Test
    public void testIsSwapTableEnableWhenManualSwapTableStartedIsTrue() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true ));
    }
}