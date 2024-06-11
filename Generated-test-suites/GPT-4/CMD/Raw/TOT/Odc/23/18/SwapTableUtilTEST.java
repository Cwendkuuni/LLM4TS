import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        assertEquals(true, SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false));

        // Edge case: Full transfer progress percentage is not completed
        assertEquals(false, SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                99.99,
                FullVerificationResult.CONSISTENT,
                false));

        // Error scenario: manualSwapTable has already started
        assertEquals(false, SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                true));

        // Error scenario: taskStatus is not running
        assertEquals(false, SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.STOPPED,
                100.0,
                FullVerificationResult.CONSISTENT,
                false));

        // Error scenario: fullVerificationResult is INCONSISTENT
        assertEquals(false, SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.INCONSISTENT,
                false));
    }
}