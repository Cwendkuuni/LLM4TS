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
    public void testTraceWithLongParameters() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(Long.toString(1), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(Long.toString(2), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(Long.toString(3), context.get(OdcConstants.ORGANIZATION_ID));
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
    }

    @Test
    public void testTraceWithNullParameters() {
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
        assertEquals(context, MDC.getCopyOfContextMap());
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.size());
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals(Long.toString(1), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(Long.toString(2), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(Long.toString(3), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testSetCurrentUser() {
        Long userId = 1L;
        Long organizationId = 2L;
        SecurityContextUtils securityContextUtils = mock(SecurityContextUtils.class);

        when(securityContextUtils.getCurrentUser()).thenReturn(userId);

        OnlineSchemaChangeContextHolder.trace(userId, null, null);
        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();
        OnlineSchemaChangeContextHolder.setCurrentUser(securityContextUtils);
        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}