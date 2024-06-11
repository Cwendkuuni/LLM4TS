
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnabled() {
        // Test valid case
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
         100.0, FullVerificationResult.CONSISTENT, false));

        // Test invalid cases
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
         99.9, FullVerificationResult.CONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PAUSED,
         100.0, FullVerificationResult.CONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
         100.0, FullVerificationResult.CONSISTENT, true));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING,
         100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test(expected = NullPointerException.class)
    public void testNullSwapTableType() {
        SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0,
         FullVerificationResult.CONSISTENT, false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTaskStatus() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, 100.0, 
        FullVerificationResult.CONSISTENT, false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFullTransferProgressPercentage() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, 
        FullVerificationResult.CONSISTENT, false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFullVerificationResult() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
        null, false);
    }

}
