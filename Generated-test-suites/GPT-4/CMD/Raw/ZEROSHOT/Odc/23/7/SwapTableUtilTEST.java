
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        //Test with all conditions being met
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false);
        
        Assert.assertTrue("Expected true when all the conditions are met", result);
        
        //Test when swapTableType is not MANUAL
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false);

        Assert.assertFalse("Expected false when swapTableType is not MANUAL", result);
        
        //Test when taskStatus is not RUNNING
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.PAUSED,
                100.0,
                FullVerificationResult.CONSISTENT,
                false);

        Assert.assertFalse("Expected false when taskStatus is not RUNNING", result);

        //Test when fullTransferProgressPercentage is not 100
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                90.0,
                FullVerificationResult.CONSISTENT,
                false);

        Assert.assertFalse("Expected false when fullTransferProgressPercentage is not 100", result);
        
        //Test when fullVerificationResult is not CONSISTENT or UNCHECK
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.INCONSISTENT,
                false);

        Assert.assertFalse("Expected false when fullVerificationResult is not CONSISTENT or UNCHECK", result);
    }
}
