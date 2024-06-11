import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Test;
import org.junit.Assert;

public class SwapTableUtilTest {

    @Test 
    public void testIsSwapTableEnable() {
        Assert.assertEquals(true, SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING,
            100d,
            FullVerificationResult.CONSISTENT,
            false));

        Assert.assertEquals(false, SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING,
            100d,
            FullVerificationResult.CONSISTENT,
            true));
    }
    
    @Test(expected = NullPointerException.class) 
    public void testIsSwapTableEnableWithNullValues() {
        SwapTableUtil.isSwapTableEnable(
            null, 
            TaskStatus.RUNNING,
            100d,
            FullVerificationResult.CONSISTENT,
            false);

        SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            null,
            100d,
            FullVerificationResult.CONSISTENT,
            false);

        SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING,
            100d,
            null,
            false);
    }
}