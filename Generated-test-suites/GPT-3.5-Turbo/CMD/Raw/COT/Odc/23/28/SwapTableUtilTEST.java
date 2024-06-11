import static org.junit.Assert.*;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test basic functionality
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertTrue(result);
    }

    // Test edge cases and exception handling scenarios
    @Test
    public void testIsSwapTableEnableEdgeCases() {
        // Test for manual swap table type with manual swap table already started
        boolean result1 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        assertFalse(result1);
        
        // Test for non-manual swap table type
        boolean result2 = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result2);
        
        // Test for task status other than running
        boolean result3 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.READY, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result3);
        
        // Test for full transfer progress percentage other than 100
        boolean result4 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9, FullVerificationResult.CONSISTENT, false);
        assertFalse(result4);
        
        // Test for full verification result other than CONSISTENT or UNCHECK
        boolean result5 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false);
        assertFalse(result5);
    }
}