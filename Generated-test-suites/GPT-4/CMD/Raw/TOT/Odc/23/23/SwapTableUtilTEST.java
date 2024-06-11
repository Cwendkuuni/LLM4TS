import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {

    // Test when SwapTableType is MANUAL and current status is RUNNING.
    @Test
    public void testIsSwapTableEnableInRunningStatus() {
        boolean actual = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.00,
                FullVerificationResult.CONSISTENT,
                false
        );
        Assert.assertTrue(actual);
    }

    // Test when SwapTableType is not MANUAL.
    @Test
    public void testIsSwapTableEnableInNonManualType() {
        boolean actual = SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTOMATIC,
                TaskStatus.RUNNING,
                100.00,
                FullVerificationResult.CONSISTENT,
                false
        );
        Assert.assertFalse(actual);
    }

    // Test when FullVerificationResult is UNCHECK.
    @Test
    public void testIsSwapTableEnableInUncheckVerificationStatus() {
        boolean actual = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.00,
                FullVerificationResult.UNCHECK,
                false
        );
        Assert.assertTrue(actual);
    }

    // Test when manualSwapTableStarted flag is true.
    @Test
    public void testIsSwapTableEnableWhenManualSwapTableStarted() {
        boolean actual = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.00,
                FullVerificationResult.CONSISTANT,
                true
        );
        Assert.assertFalse(actual);
    }

    // Test when fullTransferProgressPercentage is not 100.
    @Test
    public void testIsSwapTableEnableWhenProgressNot100() {
        boolean actual = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                90.00,
                FullVerificationResult.CONSISTANT,
                false
        );
        Assert.assertFalse(actual);
    }
}