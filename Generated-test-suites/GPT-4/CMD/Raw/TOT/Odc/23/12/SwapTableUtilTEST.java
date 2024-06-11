import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;

public class SwapTableUtilTest {

    @Test
    public void test1_IsSwapTableEnable() {
        // Test when swap table condition is fulfilled
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
        100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void test2_IsSwapTableEnable() {
        // test when ManualSwapTable previously started
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
        100.0, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void test3_IsSwapTableEnable() {
        // test when taskStatus is not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 
        100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void test4_IsSwapTableEnable() {
        // test when SwapTableType is not MANUAL
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 
        100.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void test5_IsSwapTableEnable() {
        // test when FullVerificationResult is INCONSISTENT
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
        100.0, FullVerificationResult.INCONSISTENT, false));
    }
    
    @Test
    public void test6_IsSwapTableEnable() {
        // test when fullTransferProgressPercentage is not 100%
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
        80.0, FullVerificationResult.CONSISTENT, false));
    }
}