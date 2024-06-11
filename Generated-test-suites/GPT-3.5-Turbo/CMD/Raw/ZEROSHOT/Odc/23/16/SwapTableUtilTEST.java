import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_manualSwapTableStarted_false() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_manualSwapTableStarted_true() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_invalidStatus_false() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETE, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_incompleteProgress_false() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_inconsistentResult_false() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_uncertainResult_true() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
        assertTrue(result);
    }
}