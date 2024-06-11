import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.STOPPED, 90.00, FullVerificationResult.INCONSISTENT, false));
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_FullTransferProgressPercentageNull() {
         SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, FullVerificationResult.CONSISTENT, false);
    }
    
    @Test
    public void testIsSwapTableEnable_LessThanHundredFullTransferProgressPercentage() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.00, FullVerificationResult.CONSISTENT, false));
    }
    
    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_nullFullVerificationResult() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, null, false);
    }
    
    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_NullTaskStatus() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, 100.00, FullVerificationResult.CONSISTENT, false);
    }
}