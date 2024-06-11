package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private Long userId;
    private Long organizationId;
    private Long taskId;

    @Before
    public void setup() {
        userId = 1L;
        organizationId = 2L;
        taskId = 3L;
    }

    @Test
    public void testTraceWithLongParameters() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userIdStr = userId.toString();
        String organizationIdStr = organizationId.toString();
        String taskIdStr = taskId.toString();
        OnlineSchemaChangeContextHolder.trace(userIdStr, taskIdStr, organizationIdStr);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userIdStr, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdStr, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdStr, context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidLongParameters() {
        String invalidUserIdStr = "abc";
        String invalidTaskIdStr = "xyz";
        String invalidOrganizationIdStr = "123x";
        OnlineSchemaChangeContextHolder.trace(invalidUserIdStr, invalidTaskIdStr, invalidOrganizationIdStr);
    }

    @Test
    public void testRetrace() {
        Long userId2 = 4L;
        Long organizationId2 = 5L;
        Long taskId2 = 6L;
        OnlineSchemaChangeContextHolder.trace(userId2, taskId2, organizationId2);
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        MDC.setContextMap(context);
        Map<String, String> context2 = MDC.getCopyOfContextMap();
        assertEquals(userId2.toString(), context2.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId2.toString(), context2.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId2.toString(), context2.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = IllegalStateException.class)
    public void testRetraceWithoutPreviousTrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> contextBeforeClear = MDC.getCopyOfContextMap();
        OnlineSchemaChangeContextHolder.clear();
        Map<String, String> contextAfterClear = MDC.getCopyOfContextMap();
        assertTrue(contextBeforeClear.size() > contextAfterClear.size());
        assertNull(contextAfterClear.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(contextAfterClear.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(contextAfterClear.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGetWithNullKey() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(null));
    }
}