import static org.junit.Assert.assertEquals;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Before;
import org.junit.Test;

public class SwapTableUtilTest {

  SwapTableType swapTableType;
  TaskStatus taskStatus;
  double fullTransferProgressPercentage;
  FullVerificationResult fullVerificationResult;
  boolean manualSwapTableStarted;

  @Before
  public void before() {
    swapTableType = SwapTableType.MANUAL;
    taskStatus = TaskStatus.RUNNING;
    fullTransferProgressPercentage = 100.0;
    fullVerificationResult = FullVerificationResult.CONSISTENT;
    manualSwapTableStarted = false;
  }

  @Test
  public void testIsSwapTableEnable() {
    boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
        fullVerificationResult, manualSwapTableStarted);
    assertEquals(true, result);
  }

  @Test
  public void testIsSwapTableEnableEdgeCase() {
    taskStatus = TaskStatus.ERROR;
    boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
        fullVerificationResult, manualSwapTableStarted);
    assertEquals(false, result);
  }

  @Test
  public void testIsSwapTableEnableErrorCase() {
    swapTableType = null;
    boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
        fullVerificationResult, manualSwapTableStarted);
    assertEquals(false, result);
  }

}