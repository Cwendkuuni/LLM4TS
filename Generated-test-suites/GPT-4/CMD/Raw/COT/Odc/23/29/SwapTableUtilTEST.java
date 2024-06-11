import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {
    @Test
    public void testIsSwapTableEnable() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.0, FullVerificationResult.UNCHECK, false));
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnableWithNullTransferProgressPercentage() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, null, FullVerificationResult.UNCHECK, false);
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnableWithNullSwapTableType() {
        SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnableWithNullTaskStatus() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, null, 100.0, FullVerificationResult.UNCHECK, false);
    }

    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnableWithNullFullVerificationResult() {
        SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, null, false);
    }

    @Test
    public void testIsSwapTableEnableWithProgressPercentageLessThan100() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false));
    }

    @Test
    public void testIsSwapTableEnableWithProgressPercentageGreaterThan100() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 150.0, FullVerificationResult.CONSISTENT, false));
    }
}