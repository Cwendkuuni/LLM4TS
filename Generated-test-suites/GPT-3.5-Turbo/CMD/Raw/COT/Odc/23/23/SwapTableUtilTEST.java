import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test case with all conditions met
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false);
        assertEquals(true, result);

        // Test case with some conditions not met
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false);
        assertEquals(false, result);

        // Add more test cases as needed for different scenarios
    }
}