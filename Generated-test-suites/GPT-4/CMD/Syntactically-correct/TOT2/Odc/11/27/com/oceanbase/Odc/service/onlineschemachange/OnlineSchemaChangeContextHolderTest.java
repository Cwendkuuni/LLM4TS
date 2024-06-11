package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Collections;

public class OnlineSchemaChangeContextHolderTest {

  @Before
  public void setUp() {
    OnlineSchemaChangeContextHolder.clear();
  }

  @Test 
  public void test_traceLong_nonNullValues() {
    Long userId = 1L, taskId = 2L, orgId = 3L;
    OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);
    assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    //.. more assertions
  }
  
  //.. more test cases for other methods

  @Test
  public void test_traceLong_nullValues() {
    OnlineSchemaChangeContextHolder.trace(null, null, null);
    assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    //.. more assertions
  }

  @Test (expected = IllegalArgumentException.class)
  public void test_traceString_illegalArgument() {
    OnlineSchemaChangeContextHolder.trace("xyz", "1", "1");
  }
  
  @Test
  public void test_clear() {
    OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
    OnlineSchemaChangeContextHolder.clear();
    assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    //.. more assertions
  }
}
