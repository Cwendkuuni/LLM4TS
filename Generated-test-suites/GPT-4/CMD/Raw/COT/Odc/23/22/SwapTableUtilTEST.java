import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnableHappyPath() {

        boolean result = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.CONSISTENT,
            false);

        assertTrue(result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnableWithNullSwapTableType() {
        SwapTableUtil.isSwapTableEnable(
            null, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.CONSISTENT,
            false);
    }
    
    @Test
    public void testIsSwapTableEnableNonIntegerCompletionPercent() {
        boolean result = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            100.5, 
            FullVerificationResult.CONSISTANT,
            false);

        assertTrue(result);
    }
}