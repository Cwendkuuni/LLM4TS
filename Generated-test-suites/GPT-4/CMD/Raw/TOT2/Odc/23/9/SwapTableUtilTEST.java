
package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;

/**
 * Test for SwapTableUtil.java
 */
public class SwapTableUtilTest {

    @Test
    public void isSwapTableEnableTest() {
        assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false));
                
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTOMATIC, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false));
                
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.PAUSED, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                90.0, 
                FullVerificationResult.CONSISTENT, 
                false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.INCONSISTENT, 
                false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                true));
    }           
}  
