import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case for isSwapTableEnable method with all conditions true
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        
        // Test case for isSwapTableEnable method with different conditions
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 50.0, FullVerificationResult.UNCHECK, true));
    }

    // Potential edge cases and exceptions:
    // - swapTableType is null
    // - taskStatus is null
    // - fullTransferProgressPercentage is null
    // - fullVerificationResult is null

    @Test
    public void testIsSwapTableEnableEdgeCases() {
        // Test case for edge case when swapTableType is null
        assertFalse(SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case for edge case when taskStatus is null
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, 100.0, FullVerificationResult.CONSISTENT, false));

        // Test case for edge case when fullTransferProgressPercentage is null
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, FullVerificationResult.CONSISTENT, false));

        // Test case for edge case when fullVerificationResult is null
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, null, false));
    }

}