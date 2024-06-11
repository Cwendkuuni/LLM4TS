
import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test basic functionality
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertTrue(result);
    }

    // Edge cases and exceptions to test for:
    // 1. SwapTableType is not MANUAL
    // 2. Manual swap table has already started
    // 3. Task status is not RUNNING
    // 4. Full transfer progress percentage is not 100
    // 5. Full verification result is not CONSISTENT or UNCHECK
    // 6. Combination of edge cases

    @Test
    public void testIsSwapTableEnable_SwapTableTypeNotManual() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_ManualSwapTableStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_TaskStatusNotRunning() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.FINISHED;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_FullTransferProgressPercentageNot100() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 99.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_FullVerificationResultNotConsistentOrUncheck() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_CombinationOfEdgeCases() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.FINISHED;
        Double fullTransferProgressPercentage = 99.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        boolean manualSwapTableStarted = true;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertFalse(result);
    }

}

