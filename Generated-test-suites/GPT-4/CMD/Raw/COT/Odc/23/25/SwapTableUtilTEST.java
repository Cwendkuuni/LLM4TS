
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test with all conditions being met
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false));
        // Test with the swapTableType being not MANUAL
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.NON_MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false));
        // Test with the manualSwapTableStarted being true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, true));
        // Test with the taskStatus not being RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOP, 100.0,
                FullVerificationResult.CONSISTENT, false));
        // Test with the fullTransferProgressPercentage not being 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9,
                FullVerificationResult.CONSISTENT, false));
        // Test with the fullVerificationResult being not CONSISTENT and not UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9,
                FullVerificationResult.INCONSISTENT, false));
    }
}
