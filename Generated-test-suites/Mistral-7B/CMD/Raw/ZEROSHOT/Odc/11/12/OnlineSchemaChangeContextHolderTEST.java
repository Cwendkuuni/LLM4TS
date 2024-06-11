package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder holder;

    @Before
    public void setUp() {
        holder = new OnlineSchemaChangeContextHolder();
    }

    @Test
    public void testTraceWithLongParameters() {
        long userId = 1L;
        long taskId = 2L;
        long organizationId = 3L;

        holder.trace(userId, taskId, organizationId);

        assertEquals(userId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        holder.trace(userId, taskId, organizationId);

        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);

        assertEquals(userIdL + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL + "", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");

        holder.retrace(context);

        assertEquals(context, MDC.getContextMap());
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");

        holder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");

        assertEquals("123", holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTraceWithNullParameters() {
        holder.trace(null, null, null);

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testSetCurrentUser() {
        long userId = 1L;
        long organizationId = 2L;

        holder.trace(userId, null, organizationId);

        Mockito.verify(SecurityContextUtils.class, Mockito.times(1)).setCurrentUser(userId, organizationId, null);
    }
}