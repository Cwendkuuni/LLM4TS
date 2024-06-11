import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable(){
       Boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
       assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnableWithInvalidFullTransferProgress(){
       Boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.0, FullVerificationResult.CONSISTENT, false);
       assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnableWithInvalidTaskStatus(){
       Boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.0, FullVerificationResult.CONSISTENT, false);
       assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnableWithInvalidFullVerificationResult(){
       Boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false);
       assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnableWithManualSwapTableStartedTrue(){
       Boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
       assertFalse(result);
    }

}