import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {

    // Expert 1 test case
    @Test
    public void testIsSwapTableEnable() {
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false));
    }

    // Expert 2 test case
    @Test
    public void testIsSwapTableEnableForFailedCase() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO,
                TaskStatus.IN_PROGRESS,
                100.0,
                FullVerificationResult.INCONSISTENT,
                true));
    }
    
    // Expert 3 test case
    @Test
    public void testIsSwapTableEnableForUncheckVerification() {
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.UNCHECK,
                false));
    }
}