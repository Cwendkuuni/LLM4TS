
package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false);
        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_SwapTypeNotManual() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTOMATIC,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapStarted() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                true);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_TaskNotRunning() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.PAUSED,
                100.0,
                FullVerificationResult.CONSISTENT,
                false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_ProgressNot100() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                99.0,
                FullVerificationResult.CONSISTENT,
                false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_NotConsistentOrUnchecked() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.INCONSISTENT,
                false);
        assertFalse(result);
    }

}
