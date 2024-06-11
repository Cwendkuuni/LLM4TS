import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_typicalCase() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_edgeCase() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_errorScenario() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTOMATIC, TaskStatus.COMPLETED, 100.0, FullVerificationResult.UNCHECK, false);
        assertFalse(result);
    }
}