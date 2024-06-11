package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongArguments() {
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
    public void testTraceWithStringArguments() {
        Long userId = 1L;
        String userStr = userId.toString();
        Long taskId = 2L;
        String taskStr = taskId.toString();
        Long organizationId = 3L;
        String orgStr = organizationId.toString();

        OnlineSchemaChangeContextHolder.trace(userStr, taskStr, orgStr);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userStr, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskStr, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(orgStr, context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullArguments() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(null, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        ArgumentCaptor<Map<String, String>> argCaptor = ArgumentCaptor.forClass(Map.class);
        verify(MDC).setContextMap(argCaptor.capture());
        assertEquals(context, argCaptor.getValue());
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(null, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullKey() {
        OnlineSchemaChangeChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.get(null);
    }

    @Test
    public void testGetWithValidKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        String value = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(Long.toString(2L), value);
    }
}