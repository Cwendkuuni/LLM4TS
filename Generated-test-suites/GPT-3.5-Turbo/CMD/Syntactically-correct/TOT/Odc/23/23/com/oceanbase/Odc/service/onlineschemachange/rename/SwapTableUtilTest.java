import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnableTypical() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                FullVerificationResult.CONSISTENT, false);
        assertEquals(true, result);
    }
    
    // Add more test cases for edge cases and error scenarios here
    
}