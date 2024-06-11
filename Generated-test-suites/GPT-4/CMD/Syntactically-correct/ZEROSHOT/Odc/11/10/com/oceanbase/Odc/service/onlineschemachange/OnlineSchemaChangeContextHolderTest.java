
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

  private static final Long userIdL = 123L;
  private static final Long taskIdL = 456L;
  private static final Long organizationIdL = 789L;

  private static final String userIdS = "123";
  private static final String taskIdS = "456";
  private static final String organizationIdS = "789";
  
  @Before
  public void setUp() {
    OnlineSchemaChangeContextHolder.clear();
  }

  @After
  public void tearDown() {
    OnlineSchemaChangeContextHolder.clear();
  }

  @Test
  public void testTraceWithLongParam() {
    OnlineSchemaChangeContextHolder.trace(userIdL, taskIdL, organizationIdL);
    assertEquals(userIdL.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals(taskIdL.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals(organizationIdL.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
  }

  @Test
  public void testTraceWithStringParam() {
    OnlineSchemaChangeContextHolder.trace(userIdS, taskIdS, organizationIdS);
    assertEquals(userIdS, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals(taskIdS, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals(organizationIdS, MDC.get(OdcConstants.ORGANIZATION_ID));
  }
  
  @Test
  public void testRetrace() {
    Map<String, String> context = new HashMap<>();
    context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userIdS);
    context.put(OnlineSchemaChangeContextHolder.TASK_ID, taskIdS);
    context.put(OdcConstants.ORGANIZATION_ID, organizationIdS);
    OnlineSchemaChangeContextHolder.retrace(context);
    assertEquals(userIdS, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals(taskIdS, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals(organizationIdS, MDC.get(OdcConstants.ORGANIZATION_ID));
  }

  @Test
  public void testClear() {
    OnlineSchemaChangeContextHolder.trace(userIdL, taskIdL, organizationIdL);
    OnlineSchemaChangeContextHolder.clear();
    assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
  }

  @Test
  public void testGet() {
    OnlineSchemaChangeContextHolder.trace(userIdL, taskIdL, organizationIdL);
    assertEquals(userIdL.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals(taskIdL.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals(organizationIdL.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
  }
}
