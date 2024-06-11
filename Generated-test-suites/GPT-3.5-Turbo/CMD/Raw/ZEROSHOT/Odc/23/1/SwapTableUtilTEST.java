import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        boolean result = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(true, result);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableStarted() {
        boolean result = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnable_TaskStatusNotRunning() {
        boolean result = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnable_NotFullTransferProgress() {
        boolean result = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnable_InconsistentVerificationResult() {
        boolean result = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnable_UncheckedVerificationResult() {
        boolean result = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
        assertEquals(true, result);
    }
}