package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
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

    @After
    public void tearDown() {
        holder.clear();
        MDC.clear();
    }

    @Test
    public void testTraceWithLongArguments() {
        holder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeChangeHolder.TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArguments() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        holder.trace(userId + "", taskId + "", organizationId + "");
        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullArguments() {
        holder.trace(null, null, null);
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        holder.trace(1L, 2L, 3L);
        holder.retrace(context);
        assertEquals("1", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        holder.trace(1L, 2L, 3L);
        holder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        holder.trace(1L, 2L, 3L);
        assertEquals("1", holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", holder.get(OdcConstants.ORGANIZATION_ID));
        assertNull(holder.get(OdcConstants.NON_EXISTENT_KEY));
    }
}