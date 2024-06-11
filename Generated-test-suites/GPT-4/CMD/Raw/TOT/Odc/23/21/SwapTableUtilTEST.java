import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void test_isSwapTableEnable_Success() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false);
        Assert.assertTrue(result);
    }

    @Test
    public void test_isSwapTableEnable_Failed_Due_To_Wrong_SwapTableType() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);
    }

    @Test
    public void test_isSwapTableEnable_Failed_Due_To_ManualSwapTableStarted() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, true);
        Assert.assertFalse(result);
    }

    @Test
    public void test_isSwapTableEnable_Failed_Due_To_Wrong_TaskStatus() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.0,
                FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);
    }

    @Test
    public void test_isSwapTableEnable_Failed_Due_To_Wrong_TransferPercentage() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0,
                FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);
    }

    @Test
    public void test_isSwapTableEnable_Failed_Due_To_Wrong_VerificationResult() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.INCONSISTENT, false);
        Assert.assertFalse(result);
    }
}