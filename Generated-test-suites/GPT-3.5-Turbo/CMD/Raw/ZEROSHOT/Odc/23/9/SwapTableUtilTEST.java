import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {
  
  @Test
  public void testIsSwapTableEnable() {
    SwapTableType swapTableType = SwapTableType.MANUAL;
    TaskStatus taskStatus = TaskStatus.RUNNING;
    Double fullTransferProgressPercentage = 100.0;
    FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
    boolean manualSwapTableStarted = false;

    assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
  }

  @Test
  public void testIsSwapTableEnable_ManualSwapTableStarted() {
    SwapTableType swapTableType = SwapTableType.MANUAL;
    TaskStatus taskStatus = TaskStatus.RUNNING;
    Double fullTransferProgressPercentage = 100.0;
    FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
    boolean manualSwapTableStarted = true;

    assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
  }

  @Test
  public void testIsSwapTableEnable_OtherSwapTableType() {
    SwapTableType swapTableType = SwapTableType.AUTO;
    TaskStatus taskStatus = TaskStatus.RUNNING;
    Double fullTransferProgressPercentage = 100.0;
    FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
    boolean manualSwapTableStarted = false;

    assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
  }

  @Test
  public void testIsSwapTableEnable_IncompleteProgress() {
    SwapTableType swapTableType = SwapTableType.MANUAL;
    TaskStatus taskStatus = TaskStatus.RUNNING;
    Double fullTransferProgressPercentage = 50.0;
    FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
    boolean manualSwapTableStarted = false;

    assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
  }

  @Test
  public void testIsSwapTableEnable_InconsistentVerificationResult() {
    SwapTableType swapTableType = SwapTableType.MANUAL;
    TaskStatus taskStatus = TaskStatus.RUNNING;
    Double fullTransferProgressPercentage = 100.0;
    FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
    boolean manualSwapTableStarted = false;

    assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
  }
}