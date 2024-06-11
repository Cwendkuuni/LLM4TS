import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertTrue(result);
    }

    // Edge Cases and Exception Handling
    @Test
    public void testIsSwapTableEnableEdgeCases() {
        // Edge case: swapTableType is not MANUAL
        boolean result1 = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result1);

        // Edge case: manualSwapTableStarted is true
        boolean result2 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        assertFalse(result2);

        // Edge case: TaskStatus is not RUNNING
        boolean result3 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result3);

        // Edge case: fullTransferProgressPercentage is not 100.0
        boolean result4 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result4);

        // Edge case: fullVerificationResult is not CONSISTENT or UNCHECK
        boolean result5 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INVALID, false);
        assertFalse(result5);
    }
}