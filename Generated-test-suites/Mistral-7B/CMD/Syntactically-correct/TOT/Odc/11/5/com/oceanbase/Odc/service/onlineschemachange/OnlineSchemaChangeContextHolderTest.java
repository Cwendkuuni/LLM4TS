package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

 private Map<String, String> contextBefore;

 @Before
 public void setUp() {
 contextBefore = new HashMap<>(MDC.getContextMap());
 }

 @After
 public void tearDown() {
 OnlineSchemaChangeContextHolder.clear();
 MDC.setContextMap(contextBefore);
 }

 @Test
 public void testTraceWithLongValues() {
 // given
 Long userId = 1L;
 Long taskId = 2L;
 Long organizationId = 3L;

 // when
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

 // then
 assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testTraceWithStringValues() {
 // given
 String userId = "1";
 String taskId = "2";
 String organizationId = "3";

 // when
 OnlineSchemaChangeChangeContextHolder.trace(userId, taskId, organizationId);

 // then
 assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testTraceWithNullValues() {
 // given
 OnlineSchemaChangeContextHolder.clear();

 // when
 OnlineSchemaChangeContextHolder.trace(null, null, null);

 // then
 assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testRetrace() {
 // given
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
 Map<String, String> context = OnlineSchemaChangeContextHolder.getContext();

 // when
 OnlineSchemaChangeContextHolder.retrace(context);

 // then
 assertEquals(1L, Long.parseLong(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)));
 assertEquals(2L, Long.parseLong(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID)));
 assertEquals(3L, Long.parseLong(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID)));
 }

 @Test
 public void testClear() {
 // given
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

 // when
 OnlineSchemaChangeContextHolder.clear();

 // then
 assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }
}