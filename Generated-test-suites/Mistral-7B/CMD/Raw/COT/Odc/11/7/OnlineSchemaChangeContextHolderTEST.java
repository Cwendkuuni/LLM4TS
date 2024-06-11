package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import static com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder.TASK_ID;
import static com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder.TASK_WORK_SPACE;
import static com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder. trace;
import static com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder. get;

public class OnlineSchemaChangeContextHolderTest {

 @Test
 public void testTraceWithLongValues() {
 Long userId = 1L;
 Long taskId = 2L;
 Long organizationId = 3L;
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
 assertEquals(userId.toString(), get(TASK_WORK_SPACE));
 assertEquals(taskId.toString(), get(TASK_ID));
 assertEquals(organizationId.toString(), get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testTraceWithStringValues() {
 String userId = "1";
 String taskId = "2";
 String organizationId = "3";
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
 assertEquals(userId, get(TASK_WORK_SPACE));
 assertEquals(taskId, get(TASK_ID));
 assertEquals(organizationId, get(OdcConstants.ORGANIZATION_ID));
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceWithInvalidLongValues() {
 String userId = "abc";
 String taskId = "def";
 String organizationId = "123xyz";
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
 // the exception will be thrown in the trace method when parsing userId and organizationId
 }

 @Test
 public void testRetrace() {
 Map<String, String> context = new HashMap<>();
 OnlineSchemaChangeContextHolder.retrace(context);
 assertEquals(context, MDC.getCopyOfContextMap());
 }

 @Test
 public void testClear() {
 OnlineSchemaChangeContextHolder.clear();
 assertNull(get(TASK_ID));
 assertNull(get(TASK_WORK_SPACE));
 assertNull(get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testGet() {
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
 assertEquals("1", get(TASK_WORK_SPACE));
 assertEquals("2", get(TASK_ID));
 assertEquals("3", get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testGetWithNullKey() {
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
 assertNull(get(null));
 }

}