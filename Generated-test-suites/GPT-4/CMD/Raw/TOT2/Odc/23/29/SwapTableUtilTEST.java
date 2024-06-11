import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

  @Test
  public void testIsSwapTableEnable_WhenAllConditionsAreMet() {
    assertTrue(SwapTableUtil.isSwapTableEnable(
          SwapTableType.MANUAL,
          TaskStatus.RUNNING,
          100.0,
          FullVerificationResult.CONSISTENT,
          false
    ));
  }

  @Test
  public void testIsSwapTableEnable_WhenSwapTableTypeIsNotManual() {
    assertFalse(SwapTableUtil.isSwapTableEnable(
          SwapTableType.AUTOMATIC,
          TaskStatus.RUNNING,
          100.0,
          FullVerificationResult.CONSISTENT,
          false
    ));
  }

  @Test
  public void testIsSwapTableEnable_WhenTaskStatusIsNotRunning() {
    assertFalse(SwapTableUtil.isSwapTableEnable(
          SwapTableType.MANUAL,
          TaskStatus.FINISHED,
          100.0,
          FullVerificationResult.CONSISTENT,
          false
    ));
  }

  @Test
  public void testIsSwapTableEnable_WhenFullTransferProgressIsLessThanHundred() {
    assertFalse(SwapTableUtil.isSwapTableEnable(
          SwapTableType.MANUAL,
          TaskStatus.RUNNING,
          85.5,
          FullVerificationResult.CONSISTENT,
          false
    ));
  }

  @Test
  public void testIsSwapTableEnable_WhenFullVerificationResultIsInconsistent() {
    assertFalse(SwapTableUtil.isSwapTableEnable(
          SwapTableType.MANUAL,
          TaskStatus.RUNNING,
          100.0,
          FullVerificationResult.INCONSISTENT,
          false
    ));
  }

  @Test
  public void testIsSwapTableEnable_WhenManualSwapTableStarted() {
    assertFalse(SwapTableUtil.isSwapTableEnable(
          SwapTableType.MANUAL,
          TaskStatus.RUNNING,
          100.0,
          FullVerificationResult.CONSISTENT,
          true
    ));
  }
}