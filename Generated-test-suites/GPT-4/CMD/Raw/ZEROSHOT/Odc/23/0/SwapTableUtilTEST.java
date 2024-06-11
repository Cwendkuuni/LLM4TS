
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * SwapTableUtilTest
 */
public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Test where fullTransferProgressPercentage is < 100
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse("Expected result to be false", result);

        // Test where taskStatus is not RUNNING
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FINISHED, 100.0, FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse("Expected result to be false", result);

        // Test where fullVerificationResult is CHECKED
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CHECKED, false);
        Assert.assertFalse("Expected result to be false", result);

        // Test where manualSwapTableStarted is true
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        Assert.assertFalse("Expected result to be false", result);

        // Test where swapTableType is not MANUAL
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse("Expected result to be false", result);

        // Test where all conditions for true result are met
        result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        Assert.assertTrue("Expected result to be true", result);
    }
}
