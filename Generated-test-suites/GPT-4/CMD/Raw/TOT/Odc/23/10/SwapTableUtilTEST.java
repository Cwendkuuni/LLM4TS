import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testIsSwapTableEnable() throws Exception {
         boolean test1 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00,
                 FullVerificationResult.CONSISTENT, false); 
         Assert.assertEquals(test1, true); 
                
         boolean test2 = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.00, 
                 FullVerificationResult.CONSISTENT, false);
         Assert.assertEquals(test2, false);
         
         boolean test3 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.00, 
                 FullVerificationResult.CONSISTENT, false);
         Assert.assertEquals(test3, false);
         
         boolean test4 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.00, 
                 FullVerificationResult.CONSISTENT, true);
         Assert.assertEquals(test4, false);
         
         boolean test5 = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, 
                 FullVerificationResult.INCONSISTENT, false);
         Assert.assertEquals(test5, false);
    }
}