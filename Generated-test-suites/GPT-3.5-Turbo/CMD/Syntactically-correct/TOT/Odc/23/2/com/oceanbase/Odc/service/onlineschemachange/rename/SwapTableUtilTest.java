import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_manualSwapTableStartedFalse_fullTransferProgress100() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_manualSwapTableStartedTrue_fullTransferProgress100() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_nonManualSwapTableType() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_incompleteFullTransferProgress() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_inconsistentFullVerificationResult() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_errorScenario() {
        boolean result = SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

}