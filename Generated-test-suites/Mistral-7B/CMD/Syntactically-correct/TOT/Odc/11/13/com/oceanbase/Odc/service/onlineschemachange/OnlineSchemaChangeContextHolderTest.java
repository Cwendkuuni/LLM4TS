package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder holder;
    private Map<String, String> context;

    @Before
    public void setup() {
        holder = new OnlineSchemaChangeContextHolder();
        context = new HashMap<>();
    }

    @Test
    public void testTraceWithLongParameters() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        holder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), holder.get(OdcConstants.ORGANIZATION_ID));

        verify(MDC, times(3)).put(anyString(), anyString());
        verifyNoMoreInteractions(MDC);
    }

    @Test
    public void testTraceWithStringParameters() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        holder.trace("" + userId, "" + taskId, "" + organizationId);

        assertEquals(userId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), holder.get(OdcConstants.ORGANIZATION_ID));

        verify(MDC, times(3)).put(anyString(), anyString());
        verifyNoMoreInteractions(MDC);
    }

    @Test
    public void testRetrace() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        holder.trace(userId, taskId, organizationId);

        Map<String, String> oldContext = MDC.getCopyOfContextMap();
        holder.retrace(oldContext);

        assertEquals(oldContext.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(oldContext.get(OnlineSchemaChangeContextHolder.TASK_ID), holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(oldContext.get(OdcConstants.ORGANIZATION_ID), holder.get(OdcConstants.ORGANIZATION_ID));

        verify(MDC, times(1)).setContextMap(oldContext);
        verifyNoMoreInteractions(MDC);
    }

    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        holder.trace(userId, taskId, organizationId);

        holder.clear();

        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(holder.get(OdcConstants.ORGANIZATION_ID));

        verify(MDC, times(1)).clear();
        verifyNoMoreInteractions(MDC);
    }
}