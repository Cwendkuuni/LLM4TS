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
    public void testTraceWithLongParams() {
        MDC mdc = mock(MDC.class);
        when(mdc.put(Long.toString(1L), anyString())).thenReturn(mdc);
        when(mdc.put(Long.toString(2L), anyString())).thenReturn(mdc);
        when(mdc.put(Long.toString(3L), anyString())).thenReturn(mdc);

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        Map<String, String> context = OnlineSchemaChangeContextHolder.getContext();
        assertEquals(3, context.size());
        assertEquals("2", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("1", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParams() {
        MDC mdc = mock(MDC.class);
        when(mdc.put(anyString(), anyString())).thenReturn(mdc);

        OnlineSchemaChangeContextHolder.trace("1", "2", "3");

        Map<String, String> context = OnlineSchemaChangeContextHolder.getContext();
        assertEquals(3, context.size());
        assertEquals("2", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("1", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullParams() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        Map<String, String> context = OnlineSchemaChangeContextHolder.getContext();
        assertNull(context);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> currentContext = OnlineSchemaChangeContextHolder.getContext();
        assertEquals(context, currentContext);
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(null));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> context = OnlineSchemaChangeContextHolder.getContext();
        assertNull(context);
    }
}