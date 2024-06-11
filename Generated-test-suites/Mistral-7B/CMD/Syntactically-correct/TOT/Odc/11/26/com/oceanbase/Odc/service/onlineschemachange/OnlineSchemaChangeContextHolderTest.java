package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

 private Map<String, String> contextBefore;

 @Before
 public void setUp() {
 contextBefore = new HashMap< String, String >();
 MDC.setContextMap(contextBefore);
 }

 @After
 public void tearDown() {
 OnlineSchemaChangeContextHolder.clear();
 MDC.clear();
 }

 @Test
 public void testTraceWithLongParameters() {
 Long userId = 1L;
 Long taskId = 2L;
 Long organizationId = 3L;
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
 assertNotNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 }

 @Test
 public void testTraceWithStringParameters() {
 String userId = "1";
 String taskId = "2";
 String organizationId = "3";
 OnlineSchemaChangeChangeHolder.trace(userId, taskId, organizationId);
 assertNotNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceWithInvalidLongParameters() {
 Long userId = null;
 Long taskId = null;
 Long organizationId = null;
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
 fail("Should have thrown NumberFormatException");
 }

 @Test
 public void testRetrace() {
 String key = UUID.randomUUID().toString();
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
 OnlineSchemaChangeContextHolder.retrace(contextBefore);
 assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID), contextBefore.get(OnlineSchemaChangeContextHolder.TASK_ID));
 }

 @Test
 public void testClear() {
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
 OnlineSchemaChangeContextHolder.clear();
 assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 }
}