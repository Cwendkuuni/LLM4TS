
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SwapTableUtilTest {

    private SwapTableType swapTableType;
    private TaskStatus taskStatus;
    private Double fullTransferProgressPercentage;
    private FullVerificationResult fullVerificationResult;
    private boolean manualSwapTableStarted;

    // Typical use case
    @Test
    public void swapTableEnabledTest() {
        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTENT;
        manualSwapTableStarted = false;
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    // Edge case: not manual
    @Test
    public void notManualSwapTableTest() {
        swapTableType = SwapTableType.AUTO;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTANT;
        manualSwapTableStarted = false;
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    // Edge case: manualSwapTableStarted is true
    @Test
    public void manualSwapTableStartedTest() {
        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTANT;
        manualSwapTableStarted = true;
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    // Edge case: not fullTransferProgressPercentage is not 100
    @Test
    public void notFullProgressTest() {
        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.RUNNING;
        fullTransferProgressPercentage = 95.0;
        fullVerificationResult = FullVerificationResult.CONSISTANT;
        manualSwapTableStarted = false;
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    // Error scenario: TaskStatus is not RUNNING
    @Test
    public void taskStatusNotRunningTest() {
        swapTableType = SwapTableType.MANUAL;
        taskStatus = TaskStatus.NOT_RUNNING;
        fullTransferProgressPercentage = 100.0;
        fullVerificationResult = FullVerificationResult.CONSISTANT;
        manualSwapTableStarted = false;
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }
}
