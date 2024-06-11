
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for SwapTableUtil
 */
public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        //assuming fullTransferProgressPercentage is 100% and the swap table has not started
        boolean result = SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            100.0,
            FullVerificationResult.CONSISTENT,
            false
        );
        Assert.assertTrue(result);
        
        // test when taskStatus is not RUNNING
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PENDING, 100.0,
                FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);

        // test when fullTransferProgressPercentage is less than 100
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9,
                FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);

        // test when fullVerificationResult is INCONSISTENT
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.INCONSISTENT, false);
        Assert.assertFalse(result);

        // test when manual swap table has already started
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, true);
        Assert.assertFalse(result);
        
        // test when swapTableType is not MANUAL
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0,
                FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);
    }
}
