package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Junit test for class {@link SwapTableUtil}.
 *
 */
public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_whenAllConditionsMet() {
        assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false));

        assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.UNCHECK,
                false));
    }

    @Test
    public void testIsSwapTableEnable_whenSwapTypeIsNotManual() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTOMATIC,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false));
    }

    @Test
    public void testIsSwapTableEnable_whenManualSwapTableStartedIsTrue() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                true));
    }

    @Test
    public void testIsSwapTableEnable_whenTaskStatusIsNotRunning() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.FINISHED,
                100.0,
                FullVerificationResult.CONSISTENT,
                false));
    }

    @Test
    public void testIsSwapTableEnable_whenFullTransferProgressPercentageIsNot100() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                99.9,
                FullVerificationResult.CONSISTENT,
                false));
    }

    @Test
    public void testIsSwapTableEnable_whenFullVerificationResultIsNotConsistentNorUncheck() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.INCONSISTENT,
                false));
    }
}