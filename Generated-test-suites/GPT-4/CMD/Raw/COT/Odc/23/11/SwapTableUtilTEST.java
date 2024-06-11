
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

  @Test
  public void testIsSwapTableEnable() {
    // Given
    SwapTableType swapTableType = SwapTableType.MANUAL;
    TaskStatus taskStatus = TaskStatus.RUNNING;
    Double fullTransferProgressPercentage = 100.00;
    FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
    boolean manualSwapTableStarted = false;
    // When
    boolean enable = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    // Then
    assertTrue(enable);
  }

  @Test
  public void testIsSwapTableEnableNotFullPercentage() {
    // Given
    SwapTableType swapTableType = SwapTableType.MANUAL;
    TaskStatus taskStatus = TaskStatus.RUNNING;
    Double fullTransferProgressPercentage = 75.00;
    FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
    boolean manualSwapTableStarted = false;
    // When
    boolean enable = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    // Then
    assertFalse(enable);
  }

  @Test
  public void testIsSwapTableEnableWithTaskNotRunning() {
    // Given
    SwapTableType swapTableType = SwapTableType.MANUAL;
    TaskStatus taskStatus = TaskStatus.COMPLETED;
    Double fullTransferProgressPercentage = 100.00;
    FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
    boolean manualSwapTableStarted = false;
    // When
    boolean enable = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    // Then
    assertFalse(enable);
  }

  @Test
  public void testIsSwapTableEnableWithManualSwapStarted() {
    // Given
    SwapTableType swapTableType = SwapTableType.MANUAL;
    TaskStatus taskStatus = TaskStatus.RUNNING;
    Double fullTransferProgressPercentage = 100.00;
    FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
    boolean manualSwapTableStarted = true;
    // When
    boolean enable = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    // Then
    assertFalse(enable);
  }
}
