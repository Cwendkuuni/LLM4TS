import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_WithValidParameters_ReturnTrue() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertTrue(result);
    }

    // Test with swap table type not MANUAL
    @Test
    public void testIsSwapTableEnable_WithSwapTableTypeNotManual_ReturnFalse() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    // Test with manual swap table already started
    @Test
    public void testIsSwapTableEnable_WithManualSwapTableStarted_ReturnFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    // Test with task status not RUNNING
    @Test
    public void testIsSwapTableEnable_WithTaskStatusNotRunning_ReturnFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.FINISHED;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    // Test with full transfer progress percentage not equal to 100
    @Test
    public void testIsSwapTableEnable_WithFullTransferProgressNot100_ReturnFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 50.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    // Test with full verification result not CONSISTENT or UNCHECK
    @Test
    public void testIsSwapTableEnable_WithInvalidFullVerificationResult_ReturnFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.FAILED;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

}