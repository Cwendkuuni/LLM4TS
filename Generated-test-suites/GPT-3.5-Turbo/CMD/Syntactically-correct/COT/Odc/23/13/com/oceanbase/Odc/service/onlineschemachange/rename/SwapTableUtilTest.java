import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case for regular scenario
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                100.0, FullVerificationResult.CONSISTENT, false);
        assertTrue(result);
        
        // Test case for edge case where manualSwapTableStarted is true
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                100.0, FullVerificationResult.CONSISTENT, true);
        assertFalse(result);
        
        // Test case for edge case where fullTransferProgressPercentage is not equal to 100
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                99.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
        
        // Test case for edge case where fullVerificationResult is UNCHECK
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
                100.0, FullVerificationResult.UNCHECK, false);
        assertTrue(result);
        
        // Test case for edge case where taskStatus is not RUNNING
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FAILED,
                100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }
}