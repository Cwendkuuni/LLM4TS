package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongArg() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        MDC.clear();

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArg() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        MDC.clear();

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);
        assertEquals(userIdL.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullArgs() {
        MDC.clear();

        OnlineSchemaChangeContextHolder.trace(null, null, null);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        MDC.setContextMap(context);
        assertEquals(context, MDC.getCopyOfContextMap());
    }

    @Test
    public void testClear() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();

        assertEquals(0, MDC.size());
    }

    @Test
    public void testGet() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        OnlineSchemaChangeChangeContextHolder.trace(1L, 2L, 3L);

        when(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID)).thenReturn("2");
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testGetWithNullKey() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertNull(OnlineSchemaChangeContextHolder.get(null));
    }
}