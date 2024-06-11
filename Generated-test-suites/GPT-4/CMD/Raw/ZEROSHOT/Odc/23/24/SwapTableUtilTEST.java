
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * SwapTableUtilTest
 *
 * @author Testing Team
 * @date 2023-11-09
 * @since 4.2.3
 */

public class SwapTableUtilTest {
  
  @Test
  public void testIsSwapTableEnable() {
  
    assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING,
          100.0, FullVerificationResult.CONSISTENT, false));
          
    assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
          100.0, FullVerificationResult.UNCHECK, false));
          
    assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 
          100.0, FullVerificationResult.CONSISTENT, false));
          
    assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 
          100.0, FullVerificationResult.CONSISTENT, false));
          
    assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
          90.0, FullVerificationResult.CONSISTENT, false));
          
    assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
          100.0, FullVerificationResult.INCONSISTENT, false));
          
    assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
          100.0, FullVerificationResult.CONSISTENT, true));
  }
}
