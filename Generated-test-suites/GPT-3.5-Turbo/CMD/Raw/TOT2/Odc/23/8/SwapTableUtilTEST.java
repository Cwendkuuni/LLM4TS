import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                100.0, FullVerificationResult.CONSISTENT, false));

        // Edge cases
        // When swapTableType is not MANUAL
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING,
                100.0, FullVerificationResult.CONSISTENT, false));

        // When manualSwapTableStarted is true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                100.0, FullVerificationResult.CONSISTENT, true));

        // When taskStatus is not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FINISHED,
                100.0, FullVerificationResult.CONSISTENT, false));

        // When fullTransferProgressPercentage is not 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                99.0, FullVerificationResult.CONSISTENT, false));

        // When fullVerificationResult is not CONSISTENT or UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                100.0, FullVerificationResult.INCONSISTENT, false));

        // Error scenarios
        // Avoid division by zero error when fullTransferProgressPercentage is 0
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                0.0, FullVerificationResult.CONSISTENT, false));
    }
}