package com.oceanbase.odc.service.onlineschemachange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongValues() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringValues() {
        Long userId = 1L;
        String userIdStr = userId.toString();
        Long taskId = 2L;
        String taskIdStr = taskId.toString();
        Long organizationId = 3L;
        String organizationIdStr = organizationId.toString();

        OnlineSchemaChangeContextHolder.trace(userIdStr, taskIdStr, organizationIdStr);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userIdStr, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdStr, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdStr, context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullValues() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> newContext = MDC.getCopyOfContextMap();
        assertEquals("1", newContext.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", newContext.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", newContext.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> newContext = MDC.getCopyOfContextMap();
        assertNull(newContext.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(newContext.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(newContext.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @After
    public void tearDown() {
        MDC.clear();
    }
}