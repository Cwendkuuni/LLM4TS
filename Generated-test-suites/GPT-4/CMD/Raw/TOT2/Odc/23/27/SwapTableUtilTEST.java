
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {
    
    @Test
    public void testIsSwapTableEnable(){
        // Test with all conditions met
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));

        // Test with swapTableType not MANUAL
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO,
            TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, false));

        // Test with taskStatus not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
            TaskStatus.PENDING, 100d, FullVerificationResult.CONSISTENT, false));

        // Test with progress percentage not equal to 100 
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
            TaskStatus.RUNNING, 99d, FullVerificationResult.CONSISTENT, false));
   
        // Test with FullVerificationResult not CONSISTENT or UNCHECK 
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
            TaskStatus.RUNNING, 100d, FullVerificationResult.INCONSISTENT, false));

        // Test with manualSwapTableStarted as true 
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL,
            TaskStatus.RUNNING, 100d, FullVerificationResult.CONSISTENT, true));
    }
}
