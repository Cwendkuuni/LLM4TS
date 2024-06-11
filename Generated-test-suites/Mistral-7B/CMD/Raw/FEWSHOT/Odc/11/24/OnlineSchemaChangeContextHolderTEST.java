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
    public void testTraceWithLongArguments() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        MDC mdc = mock(MDC.class);
        when(mdc.put(eq(TASK_WORK_SPACE), eq(userId.toString()))).thenReturn(null);
        when(mdc.put(eq(TASK_ID), eq(taskId.toString()))).thenReturn(null);
        when(mdc.put(eq(OdcConstants.ORGANIZATION_ID), eq(organizationId.toString()))).thenReturn(null);

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = OnlineSchemaChangeContextHolder.getContext();
        assertEquals(userId.toString(), context.get(TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArguments() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        Long userIdL = (userId == null ? null : Long.parseLong(userId));
        Long taskIdL = (taskId == null ? null : Long.parseLong(taskId));
        Long organizationIdL = (organizationId == null ? null : Long.parseLong(organizationId));

        MDC mdc = mock(MDC.class);
        when(mdc.put(eq(TASK_WORK_SPACE), eq(userIdL.toString()))).thenReturn(null);
        when(mdc.put(eq(TASK_ID), eq(taskIdL.toString()))).thenReturn(null);
        when(mdc.put(eq(OdcConstants.ORGANIZATION_ID), eq(organizationIdL.toString()))).thenReturn(null);

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = OnlineSchemaChangeContextHolder.getContext();
        assertEquals(userId, context.get(TASK_WORK_SPACE));
        assertEquals(taskId, context.get(TASK_ID));
        assertEquals(organizationId, context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(TASK_WORK_SPACE, "1");
        context.put(TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> currentContext = MDC.getCopyOfContextMap();
        assertEquals(context, currentContext);
    }

    @Test
    public void testClear() {
        MDC.put(TASK_WORK_SPACE, "1");
        MDC.put(TASK_ID, "2");
        MDC.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(TASK_WORK_SPACE));
        assertNull(context.get(TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put(TASK_WORK_SPACE, "1");
        MDC.put(TASK_ID, "2");
        MDC.put(OdcConstants.ORGANIZATION_ID, "3");

        assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.NON_EXISTENT_KEY));
    }
}