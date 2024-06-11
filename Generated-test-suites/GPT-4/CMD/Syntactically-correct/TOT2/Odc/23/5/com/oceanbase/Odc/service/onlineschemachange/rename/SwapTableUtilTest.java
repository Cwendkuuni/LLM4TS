package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {  

  @Test
  public void testIsSwapTableEnabled_ValidInputs() {
    boolean result = SwapTableUtil.isSwapTableEnable(
      SwapTableType.MANUAL, 
      TaskStatus.RUNNING, 
      100.0, 
      FullVerificationResult.CONSISTENT, 
      false
    );
    
    assertTrue("Expected True, but received False", result);
  }

  @Test
  public void testIsSwapTableEnabled_InvalidSwapTableType() {
    boolean result = SwapTableUtil.isSwapTableEnable(
      SwapTableType.AUTO, //Invalid input
      TaskStatus.RUNNING, 
      100.0, 
      FullVerificationResult.CONSISTENT, 
      false
    );
    
    assertFalse("Expected False, but received True", result);
  }

  @Test
  public void testIsSwapTableEnabled_ManualSwapTableAlreadyStarted() {
    boolean result = SwapTableUtil.isSwapTableEnable(
      SwapTableType.MANUAL, 
      TaskStatus.RUNNING, 
      100.0, 
      FullVerificationResult.CONSISTENT, 
      true //Invalid input - swap table already started
    );
    
    assertFalse("Expected False, but received True", result);
  }

  @Test
  public void testIsSwapTableEnabled_InconsistentFullVerificationResult() {
    boolean result = SwapTableUtil.isSwapTableEnable(
      SwapTableType.MANUAL, 
      TaskStatus.RUNNING, 
      100.0, 
      FullVerificationResult.INCONSISTENT, //Invalid input - Inconsistent result
      false
    );
    
    assertFalse("Expected False, but received True", result);
  }
  
  @Test
  public void testIsSwapTableEnabled_CompletePercentageNot100() {
    boolean result = SwapTableUtil.isSwapTableEnable(
      SwapTableType.MANUAL, 
      TaskStatus.RUNNING, 
      90.0, //Invalid input - Not 100 percent
      FullVerificationResult.CONSISTENT, 
      false
    );
    
    assertFalse("Expected False, but received True", result);
  }
  
  @Test
  public void testIsSwapTableEnabled_SwapTableUnchecked() {

    boolean result = SwapTableUtil.isSwapTableEnable(
      SwapTableType.MANUAL,
      TaskStatus.RUNNING,
      100.0,
      FullVerificationResult.UNCHECK,
      false
    );

    assertTrue("Expected True, but received False", result);
  }
}