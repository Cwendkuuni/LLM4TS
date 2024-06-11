import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Edge cases
        // Verify minimum fullTransferProgressPercentage
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 0.0, FullVerificationResult.CONSISTENT, false));

        // Verify maximum fullTransferProgressPercentage
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Error scenarios
        // Invalid swapTableType
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Invalid taskStatus
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.FAILED, 100.0, FullVerificationResult.CONSISTENT, false));

        // Invalid fullTransferProgressPercentage - not equal to 100
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false));

        // Invalid fullVerificationResult
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INVALID, false));

        // Manual swapTable already started
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
    }
}