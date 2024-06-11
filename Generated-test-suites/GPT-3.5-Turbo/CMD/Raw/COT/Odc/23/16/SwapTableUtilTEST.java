
import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test scenario where swapTableType is MANUAL, manualSwapTableStarted is false, taskStatus is RUNNING, fullTransferProgressPercentage is 100, and fullVerificationResult is CONSISTENT
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertTrue(result);
        
        // Add more test cases as needed
    }
    
    @Test
    public void testIsSwapTableEnableEdgeCases() {
        // Test edge case: Different input values
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.FINISHED, 50.0, FullVerificationResult.UNCHECK, true);
        assertFalse(result);
        
        // Test edge case: Null input values
        try {
            SwapTableUtil.isSwapTableEnable(null, null, null, null, false);
            fail("Exception not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
        
        // Add more edge cases and exception handling scenarios as needed
    }
}
