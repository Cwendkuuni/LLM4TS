import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case 1: All conditions met
        boolean result1 = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(true, result1);

        // Test case 2: Different swapTableType
        boolean result2 = SwapTableUtil.isSwapTableEnable(
            SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(false, result2);

        // Test case 3: Different taskStatus
        boolean result3 = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, TaskStatus.SUCCESS, 100.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(false, result3);

        // Test case 4: Progress percentage less than 100
        boolean result4 = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(false, result4);

        // Test case 5: Different fullVerificationResult
        boolean result5 = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.FAILED, false);
        assertEquals(false, result5);

        // Test case 6: Manual swapTableStarted is true
        boolean result6 = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        assertEquals(false, result6);
    }
}