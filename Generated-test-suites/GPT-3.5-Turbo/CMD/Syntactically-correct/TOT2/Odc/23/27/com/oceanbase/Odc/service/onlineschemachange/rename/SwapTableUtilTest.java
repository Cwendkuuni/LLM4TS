import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Edge case: swapTableType is not MANUAL
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Edge case: manualSwapTableStarted is true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));

        // Edge case: fullTransferProgressPercentage is not 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false));

        // Edge case: fullVerificationResult is not CONSISTENT or UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INVALID, false));

        // Error scenario: taskStatus is not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FAILED, 100.0, FullVerificationResult.CONSISTENT, false));
    }
}