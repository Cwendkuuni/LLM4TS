package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongParameters() {
        long userId = 1L;
        long taskId = 2L;
        long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParametersNull() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> currentContext = MDC.getCopyOfContextMap();
        assertEquals(context, currentContext);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetraceWithNullMap() {
        OnlineSchemaChangeContextHolder.retrace(null);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        String key = OnlineSchemaChangeContextHolder.TASK_ID;
        String value = "expectedValue";
        MDC.put(key, value);

        assertEquals(value, OnlineSchemaChangeContextHolder.get(key));
    }

    @Test
    public void testGetWithNullKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertNull(OnlineSchemaChangeContextHolder.get(null));
    }
}