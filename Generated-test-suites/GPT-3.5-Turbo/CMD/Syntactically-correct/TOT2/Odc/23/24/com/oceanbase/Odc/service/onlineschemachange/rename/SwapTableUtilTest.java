import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

  @Test
  public void testIsSwapTableEnable() {
    // Typical use case
    assertTrue(SwapTableUtil.isSwapTableEnable(
      SwapTableType.MANUAL,
      TaskStatus.RUNNING,
      100.0,
      FullVerificationResult.CONSISTENT,
      false
    ));

    // Edge case: Check when not MANUAL type
    assertFalse(SwapTableUtil.isSwapTableEnable(
      SwapTableType.AUTO,
      TaskStatus.RUNNING,
      100.0,
      FullVerificationResult.CONSISTENT,
      false
    ));

    // Edge case: Check when manualSwapTableStarted is true
    assertFalse(SwapTableUtil.isSwapTableEnable(
      SwapTableType.MANUAL,
      TaskStatus.RUNNING,
      100.0,
      FullVerificationResult.CONSISTENT,
      true
    ));

    // Edge case: Check when taskStatus not RUNNING
    assertFalse(SwapTableUtil.isSwapTableEnable(
      SwapTableType.MANUAL,
      TaskStatus.FAILED,
      100.0,
      FullVerificationResult.CONSISTENT,
      false
    ));

    // Edge case: Check when fullTransferProgressPercentage not equal to 100
    assertFalse(SwapTableUtil.isSwapTableEnable(
      SwapTableType.MANUAL,
      TaskStatus.RUNNING,
      99.0,
      FullVerificationResult.CONSISTENT,
      false
    ));

    // Edge case: Check when fullVerificationResult not CONSISTENT or UNCHECK
    assertFalse(SwapTableUtil.isSwapTableEnable(
      SwapTableType.MANUAL,
      TaskStatus.RUNNING,
      100.0,
      FullVerificationResult.VERIFIED,
      false
    ));
  }
}