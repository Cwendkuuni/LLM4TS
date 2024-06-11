package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;

public class SwapTableUtilTest {
  
   @Test
   public void testIsSwapTableEnable() {
      
      //Typical use case
      assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
                                                TaskStatus.RUNNING,
                                                Double.valueOf(100),
                                                FullVerificationResult.CONSISTENT,
                                                false));
      
      //Edge case where the transfer progress is not 100%
      assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
                                                TaskStatus.RUNNING,
                                                Double.valueOf(90),
                                                FullVerificationResult.CONSISTENT,
                                                false));
      
      //Edge case where the task status is not RUNNING
      assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
                                                TaskStatus.PENDING,
                                                Double.valueOf(100),
                                                FullVerificationResult.CONSISTENT,
                                                false));
      
      //Error scenario where the FullVerificationResult is not CONSISTENT or UNCHECK
      assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, 
                                                TaskStatus.RUNNING,
                                                Double.valueOf(100),
                                                FullVerificationResult.INCONSISTENT,
                                                false));
   }
}