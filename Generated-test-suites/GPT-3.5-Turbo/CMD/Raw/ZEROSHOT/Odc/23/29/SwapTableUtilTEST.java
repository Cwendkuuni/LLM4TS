import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false);
        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_Manual() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9,
                FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_Manual_Started() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, true);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_Auto() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_TaskNotRunning() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PENDING, 100.0,
                FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_ProgressNot100() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9,
                FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_InconsistentVerificationResult() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.INCONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_NullVerificationResult() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                null, false);
        assertFalse(result);
    }
}