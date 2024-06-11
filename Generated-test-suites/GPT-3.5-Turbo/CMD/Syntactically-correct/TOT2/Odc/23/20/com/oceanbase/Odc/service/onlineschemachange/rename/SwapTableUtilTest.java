import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case where all conditions are met
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Edge cases and error scenarios
        // Edge Case: If swapTableType is not MANUAL, should return false
        swapTableType = SwapTableType.AUTO;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Edge Case: If taskStatus is not RUNNING, should return false
        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.FAILED;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Edge Case: If fullTransferProgressPercentage is not 100, should return false
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 99.0;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Edge Case: If fullVerificationResult is not CONSISTENT or UNCHECK, should return false
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.INCONSISTENT;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

        // Edge Case: If manualSwapTableStarted is true, should return false
        fullVerificationResult = FullVerificationResult.CONSISTENT;
        manualSwapTableStarted = true;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

    }
}