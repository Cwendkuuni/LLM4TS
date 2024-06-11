import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case 1: swapTableType = MANUAL, taskStatus = RUNNING, fullTransferProgressPercentage = 100, fullVerificationResult = CONSISTENT, manualSwapTableStarted = false
        boolean result1 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(true, result1);

        // Test case 2: swapTableType = MANUAL, taskStatus = RUNNING, fullTransferProgressPercentage = 90, fullVerificationResult = UNCHECK, manualSwapTableStarted = true
        boolean result2 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.0, FullVerificationResult.UNCHECK, true);
        assertEquals(false, result2);

        // Test case 3: swapTableType = AUTO, taskStatus = RUNNING, fullTransferProgressPercentage = 100, fullVerificationResult = INCONSISTENT, manualSwapTableStarted = false
        boolean result3 = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false);
        assertEquals(false, result3);
    }

}