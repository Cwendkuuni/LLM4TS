import org.junit.Test;

import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnableNotManual() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnableManualStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnableNotRunning() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.PENDING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnableProgressLessThan100() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 90.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnableInconsistentVerification() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnableUncheckedVerification() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }
}