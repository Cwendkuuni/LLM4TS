
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {
    @Test
    public void testIsSwapTableEnable() {
        //Test for true return
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false));

        //Test for false return
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                true));
    }

    @Test(expected = NullPointerException.class)
    public void testNullSwapTableType() {
        SwapTableUtil.isSwapTableEnable(
                null, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTaskStatus() {
        SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                null, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFullTransferProgressPercentage() {
        SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                null, 
                FullVerificationResult.CONSISTENT, 
                false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFullVerificationResult() {
        SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0, 
                null, 
                false);
    }
}
