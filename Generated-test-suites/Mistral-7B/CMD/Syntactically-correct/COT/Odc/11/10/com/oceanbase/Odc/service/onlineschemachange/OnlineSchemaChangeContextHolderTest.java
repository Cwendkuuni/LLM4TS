package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongParameters() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, context.get(OdcConstants.ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidLongParameters() {
        OnlineSchemaChangeContextHolder.trace("invalidUserId", "taskId", "organizationId");
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "userId");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskId");
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(context);
        Map<String, String> contextAfterRetrace = MDC.getCopyOfContextMap();
        assertEquals(context, contextAfterRetrace);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        Map<String, String> contextAfterClear = MDC.getCopyOfContextMap();
        assertNull(contextAfterClear);
    }
}