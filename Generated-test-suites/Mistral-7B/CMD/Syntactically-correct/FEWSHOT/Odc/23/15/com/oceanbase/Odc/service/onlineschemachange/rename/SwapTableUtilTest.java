package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_TaskRunning_FullTransferProgress100_ConsistentFullVerificationResult() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_TaskRunning_FullTransferProgress100_UncheckedFullVerificationResult() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_TaskRunning_FullTransferProgressLessThan100_ConsistentFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_TaskRunning_FullTransferProgressLessThan100_UncheckedFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_TaskRunning_FullTransferProgress100_InconsistentFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_TaskRunning_FullTransferProgress100_ManualSwapTableStarted() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_TaskRunning_FullTransferProgress100_ConsistentFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_TaskRunning_FullTransferProgress100_UncheckedFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_TaskRunning_FullTransferProgressLessThan100_ConsistentFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_TaskRunning_FullTransferProgressLessThan100_UncheckedFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 50.0, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_TaskRunning_FullTransferProgress100_InconsistentFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_TaskRunning_FullTransferProgress100_ManualSwapTableStarted() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_NotRunning_FullTransferProgress100_ConsistentFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_NotRunning_FullTransferProgress100_UncheckedFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_NotRunning_FullTransferProgressLessThan100_ConsistentFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 50.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_NotRunning_FullTransferProgressLessThan100_UncheckedFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 50.0, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_NotRunning_FullTransferProgress100_InconsistentFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.INCONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_NotRunning_FullTransferProgress100_ManualSwapTableStarted() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_NotRunning_FullTransferProgress100_ConsistentFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_NotRunning_FullTransferProgress100_UncheckedFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.COMPLETED, 100.0, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_NotRunning_FullTransferProgressLessThan100_ConsistentFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.COMPLETED, 50.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_NotRunning_FullTransferProgressLessThan100_UncheckedFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.COMPLETED, 50.0, FullVerificationResult.UNCHECK, false));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_NotRunning_FullTransferProgress100_InconsistentFullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.COMPLETED, 100.0, FullVerificationResult.INCONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_NonManualSwapTable_NotRunning_FullTransferProgress100_ManualSwapTableStarted() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_Null_FullTransferProgressPercentage() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTable_Null_FullVerificationResult() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, null, false));
    }
}