package com.oceanbase.odc.service.onlineschemachange.rename;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class SwapTableUtilTest {

  private SwapTableType mockSwapTableType;
  
  private TaskStatus mockTaskStatus;

  private FullVerificationResult mockFullVerificationResult;

  @Before
  public void setUp() {
    mockSwapTableType = mock(SwapTableType.class);
    mockTaskStatus = mock(TaskStatus.class);
    mockFullVerificationResult = mock(FullVerificationResult.class);
  }

  @Test
  public void isSwapTableEnableTest() {
    boolean result = SwapTableUtil.isSwapTableEnable(mockSwapTableType, mockTaskStatus, 100.0, mockFullVerificationResult, false);
    Assert.assertFalse(result);
  }
  
  @Test(expected = NullPointerException.class)
  public void isSwapTableEnableNullInputTest() {
    // test with null values
    SwapTableUtil.isSwapTableEnable(null, null, null, null, false); 
  }
}