import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.0,
                FullVerificationResult.CONSISTENT, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9,
                FullVerificationResult.CONSISTENT, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.INCONSISTENT, false));

        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false));

        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.UNCHECK, false));

        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, true));
    }

}
