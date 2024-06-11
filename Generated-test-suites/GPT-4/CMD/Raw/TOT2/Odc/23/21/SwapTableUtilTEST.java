
import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsSwapTableDisable() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsSwapTableWhenNotRunning() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.PENDING, 100.0, FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsSwapTableLessThanFullProgress() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsSwapTableWhenNotConsistentAndNotUnchecked() {
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false);
        Assert.assertFalse(result);
    }
}
