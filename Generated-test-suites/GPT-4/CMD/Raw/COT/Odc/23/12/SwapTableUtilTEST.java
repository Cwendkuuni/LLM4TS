import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 
 * @date 
 * @since 
 */
public class SwapTableUtilTest {
    
    @Test
    public void isSwapTableEnableTest() {
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(
        		SwapTableType.MANUAL, 
        		TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false
        ));
        
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
        		SwapTableType.AUTO, 
        		TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false
        ));
             
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
        		SwapTableType.MANUAL, 
        		TaskStatus.FAILED,
                100.0,
                FullVerificationResult.CONSISTENT,
                false
        ));    
        
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
        		SwapTableType.MANUAL, 
        		TaskStatus.RUNNING,
                99.9,
                FullVerificationResult.CONSISTENT,
                false
        ));
             
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
        		SwapTableType.MANUAL, 
        		TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.INCONSISTENT,
                false
        ));
             
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(
        		SwapTableType.MANUAL, 
        		TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                true
        ));
    }
}