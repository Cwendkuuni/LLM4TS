import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        // Edge cases
        // FullTransferProgressPercentage is not equal to 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false));

        // FullVerificationResult is different from CONSISTENT or UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));

        // TaskStatus is not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false));

        // ManualSwapTableStarted is true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));

        // Exception handling scenarios
        try {
            SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
            fail("Null swapTableType should throw exception");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }
}