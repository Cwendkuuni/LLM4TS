import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        assertFalse("Case: Manual swap table type, manual swap table started, task status running, full transfer progress not 100, FullVerificationResult consistent", 
                SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, true));
        
        assertTrue("Case: Manual swap table type, manual swap table not started, task status running, full transfer progress 100, FullVerificationResult consistent", 
                SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        
        assertFalse("Case: Manual swap table type, manual swap table not started, task status running, full transfer progress 100, FullVerificationResult not consistent", 
                SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));
        
        assertFalse("Case: Manual swap table type, manual swap table not started, task status not running, full transfer progress 100, FullVerificationResult consistent", 
                SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false));
        
        assertFalse("Case: Automatic swap table type, manual swap table not started, task status running, full transfer progress 100, FullVerificationResult consistent", 
                SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
    }

}