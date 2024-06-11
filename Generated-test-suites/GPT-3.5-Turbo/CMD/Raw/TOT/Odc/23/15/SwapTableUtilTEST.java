import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_true() {
        // Typical use case
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false);
        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_false() {
        // Edge case
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0,
                FullVerificationResult.CONSISTENT, true);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_error() {
        // Error scenario
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO, TaskStatus.FAILED, 100.0,
                FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }
}