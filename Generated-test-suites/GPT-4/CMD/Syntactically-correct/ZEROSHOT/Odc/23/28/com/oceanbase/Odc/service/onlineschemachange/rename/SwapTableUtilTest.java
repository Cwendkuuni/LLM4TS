package com.oceanbase.odc.service.onlineschemachange.rename;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
        
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTOMATIC, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
        
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.PAUSED, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
        
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                99.9, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
        
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.INCONSISTENT, 
                false)
        );
        
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                true)
        );
    }
}