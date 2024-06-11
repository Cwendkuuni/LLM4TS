package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongValues() {
        Long userId = 1L;
        Long taskId = 10L;
        Long organizationId = 5L;

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
    public void testTraceWithStringValues() {
        Long userId = 1L;
        String userIdStr = userId.toString();
        Long taskId = 10L;
        String taskIdStr = taskId.toString();
        Long organizationId = 5L;
        String organizationIdStr = organizationId.toString();

        OnlineSchemaChangeContextHolder.trace(userIdStr, taskIdStr, organizationIdStr);
        Map<String, String> context = MDC.getCopyOfContextMap();

        assertEquals(userIdStr, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdStr, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdStr, context.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullValues() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        Map<String, String> context = MDC.getCopyOfContextMap();

        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "10");
        context.put(OdcConstants.ORGANIZATION_ID, "5");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("10", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("5", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 10L, 5L);
        Map<String, String> context = MDC.getCopyOfContextMap();

        assertEquals(1L, Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)));
        assertEquals(10L, Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_ID)));
        assertEquals(5L, Long.parseLong(context.get(OdcConstants.ORGANIZATION_ID)));

        OnlineSchemaChangeContextHolder.clear();
        Map<String, String> contextAfterClear = MDC.getCopyOfContextMap();

        assertNull(contextAfterClear.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(contextAfterClear.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(contextAfterClear.get(OdcConstants.ORGANIZATION_ID));
    }
}