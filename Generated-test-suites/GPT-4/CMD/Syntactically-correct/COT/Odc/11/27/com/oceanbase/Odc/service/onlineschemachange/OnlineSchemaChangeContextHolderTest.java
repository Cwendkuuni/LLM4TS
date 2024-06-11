package com.oceanbase.odc.service.onlineschemachange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

  @Test
  public void testTraceLong() {
    OnlineSchemaChangeContextHolder.trace(1000L, 2000L, 3000L);
    assertEquals("1000", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("2000", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals("3000", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
  }

  @Test
  public void testTraceString() {
    OnlineSchemaChangeContextHolder.trace("1000", "2000", "3000");
    assertEquals("1000", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("2000", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals("3000", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
  }

  @Test
  public void testRetrace() {
    Map<String, String> context = new HashMap<>();
    context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1000");
    context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2000");
    context.put(OdcConstants.ORGANIZATION_ID, "3000");
    OnlineSchemaChangeContextHolder.retrace(context);
    assertEquals("1000", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("2000", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals("3000", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
  }
    
  @Test
  public void testClear() {
    OnlineSchemaChangeContextHolder.trace("1000", "2000", "3000");
    OnlineSchemaChangeContextHolder.clear();
    assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
  }

  @Test
  public void testGet() {
    OnlineSchemaChangeContextHolder.trace("1000", "2000", "3000");
    assertEquals("1000", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("2000", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals("3000", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
  }

  // Edge case: If a null String is passed, it should be converted to a Long
  @Test
  public void testTraceStringWithNull() {
    OnlineSchemaChangeContextHolder.trace(null, "2000", "3000");
    assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("2000", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals("3000", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
  }

  // Edge case: If an invalid String is passed that can't be parsed into a Long, a NumberFormatException should be thrown
  @Test(expected = NumberFormatException.class)
  public void testTraceStringWithInvalidNumber() {
    OnlineSchemaChangeContextHolder.trace("invalid_number", "2000", "3000");
  }

  // Edge case: If an invalid key is passed, null should be returned
  @Test
  public void testGetWithInvalidKey() {
    assertNull(OnlineSchemaChangeContextHolder.get("invalid_key"));
  }
    
}
