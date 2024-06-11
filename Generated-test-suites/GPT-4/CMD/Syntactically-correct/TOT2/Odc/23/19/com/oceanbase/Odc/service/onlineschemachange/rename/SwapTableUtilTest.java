package com.oceanbase.odc.service.onlineschemachange.rename;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;

public class SwapTableUtilTest{

@Test   
public void testIsSwapTableEnable_Case1() {  
  boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
  assertEquals(true, result);
}

@Test   
public void testIsSwapTableEnable_Case2() {  
  boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
  assertEquals(false, result);
}

@Test   
public void testIsSwapTableEnable_Case3() {  
  boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.NOT_RUNNING, 99.9, FullVerificationResult.CONSISTENT, true);
  assertEquals(false, result);
}

@Test   
public void testIsSwapTableEnable_Case4() {  
  boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.NOT_CONSISTENT, false);
  assertEquals(false, result);
}

@Test   
public void testIsSwapTableEnable_Case5() {  
  boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
  assertEquals(true, result);
}

}