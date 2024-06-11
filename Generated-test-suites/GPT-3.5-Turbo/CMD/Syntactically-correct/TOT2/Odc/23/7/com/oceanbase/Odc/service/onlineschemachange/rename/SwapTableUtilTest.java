import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Edge cases
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, true));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.FINISHED, 100.0, FullVerificationResult.CONSISTENT, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
    }
}