package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void testTraceWithValidParameters() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        holder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), holder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullParameters() {
        holder.trace(null, null, null);

        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(holder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        holder.trace(userId, taskId, organizationId);

        assertEquals(userId, holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, holder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        holder.trace(1L, 2L, 3L);
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeChangeHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        holder.retrace(context);

        assertEquals(context, MDC.getCopyOfContextMap());
        assertEquals(holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), "1");
        assertEquals(holder.get(OnlineSchemaChangeContextHolder.TASK_ID), "2");
        assertEquals(holder.get(OdcConstants.ORGANIZATION_ID), "3");
    }

    @Test
    public void testClear() {
        holder.trace(1L, 2L, 3L);

        holder.clear();

        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(holder.get(OdcConstants.ORGANIZATION_ID));
    }
}