import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case with all conditions met
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                100.0, FullVerificationResult.CONSISTENT, false));

        // Test case with swapTableType not MANUAL
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING,
                100.0, FullVerificationResult.CONSISTENT, false));

        // Test case with manualSwapTableStarted set to true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                100.0, FullVerificationResult.CONSISTENT, true));

        // Test case with taskStatus not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FINISHED,
                100.0, FullVerificationResult.CONSISTENT, false));

        // Test case with fullTransferProgressPercentage not equal to 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                99.0, FullVerificationResult.CONSISTENT, false));

        // Test case with fullVerificationResult not CONSISTENT or UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                100.0, FullVerificationResult.INVALID, false));
    }
}