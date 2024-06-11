import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

       // Edge case - fullTransferProgressPercentage is not 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false));

        // Edge case - fullTransferProgressPercentage is 100 but task status is not running
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false));

        // Error scenario - invalid swapTableType
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Error scenario - fullTransferProgressPercentage is negative
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, -50.0, FullVerificationResult.CONSISTENT, false));

        // Error scenario - fullVerificationResult is null
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, null, false));
    }
}