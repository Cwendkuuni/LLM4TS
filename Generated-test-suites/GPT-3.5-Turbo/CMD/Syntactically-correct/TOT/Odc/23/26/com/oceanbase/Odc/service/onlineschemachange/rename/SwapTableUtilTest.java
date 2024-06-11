import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        
        // Edge case
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9, FullVerificationResult.CONSISTENT, false));
        
        // Error scenario
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
    }
    
    // Add more test cases for other methods if needed
    
}