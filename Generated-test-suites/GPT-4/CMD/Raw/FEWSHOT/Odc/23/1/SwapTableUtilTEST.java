import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SwapTableUtilTest {
    @Test
    public void isSwapTableEnableTest() {
        assertEquals(true, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, false));
        assertEquals(true, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.UNCHECK, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.99, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.INCONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, true));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, false));
        assertEquals(false, SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.00, FullVerificationResult.CONSISTENT, false));
    }
}