package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> context;

    @Before
    public void init() {
        context = new HashMap<>();
        MDC.setContextMap(context);
    }

    @Test
    public void testTraceWithLongArguments() {
        when(context.get(OnlineSchemaChangeContextHolder.TASK_ID)).thenReturn(null);
        when(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).thenReturn(null);
        when(context.get(OdcConstants.ORGANIZATION_ID)).thenReturn(null);

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals("3", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(null));

        context.clear();
    }

    @Test
    public void testTraceWithStringArguments() {
        when(context.get(OnlineSchemaChangeContextHolder.TASK_ID)).thenReturn(null);
        when(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).thenReturn(null);
        when(context.get(OdcConstants.ORGANIZATION_ID)).thenReturn(null);

        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(null));

        context.clear();
    }

    @Test
    public void testTraceWithNullArguments() {
        when(context.get(OnlineSchemaChangeContextHolder.TASK_ID)).thenReturn(null);
        when(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).thenReturn(null);
        when(context.get(OdcConstants.ORGANIZATION_ID)).thenReturn(null);

        OnlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(null));

        context.clear();
    }

    @Test
    public void testTraceWithSameArguments() {
        when(context.get(OnlineSchemaChangeContextHolder.TASK_ID)).thenReturn(null);
        when(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).thenReturn(null);
        when(context.get(OdcConstants.ORGANIZATION_ID)).thenReturn(null);

        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        context.clear();
    }

    @Test
    public void testRetrace() {
        when(context.get(OnlineSchemaChangeContextHolder.TASK_ID)).thenReturn("1");
        when(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).thenReturn("2");
        when(context.get(OdcConstants.ORGANIZATION_ID)).thenReturn("3");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(null));

        context.clear();
    }

    @Test
    public void testClear() {
        when(context.get(OnlineSchemaChangeContextHolder.TASK_ID)).thenReturn("1");
        when(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).thenReturn("2");
        when(context.get(OdcConstants.ORGANIZATION_ID)).thenReturn("3");

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(null));

        context.clear();
    }
}