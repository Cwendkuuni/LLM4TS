import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
                TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertTrue(result);

        // Edge case: check for successful swap table with different FullVerificationResult
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
                TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
        assertTrue(result);

        // Edge case: manualSwapTableStarted is true
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
                TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        assertFalse(result);

        // Edge case: taskStatus is not RUNNING
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
                TaskStatus.FINISHED, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);

        // Edge case: fullTransferProgressPercentage is not 100.0
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
                TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);

        // Error scenario: swapTableType is not MANUAL
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO,
                TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }
}