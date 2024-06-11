import org.junit.Test;
import org.junit.Assert;

public class SwapTableUtilTest {
 
    @Test
    public void testIsSwapTableEnable() {
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, false));
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.UNCHECK, false));
     
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, false));
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.00, FullVerificationResult.UNCHECK, false));
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.99, FullVerificationResult.CONSISTENT, false));
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.INCONSISTENT, false));
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, true));
    }

    @Test
    public void testIsSwapTableEnableWithEdgeConditions() {
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.99, FullVerificationResult.CONSISTENT, false));
    }
   
    @Test(expected=NullPointerException.class)
    public void testIsSwapTableEnableWithNullArguments() {
        SwapTableUtil.isSwapTableEnable(null, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, false);
    }
}