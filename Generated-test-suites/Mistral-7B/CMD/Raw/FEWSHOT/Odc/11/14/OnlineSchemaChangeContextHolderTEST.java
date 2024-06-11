package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongParameters() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        Long userId = Long.parseLong("1");
        Long taskId = Long.parseLong("2");
        Long organizationId = Long.parseLong("3");
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullParameters() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("123", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testGetNullKey() {
        assertNull(OnlineSchemaChangeContextHolder.get(null));
    }

    @Test
    public void testGetEmptyStringKey() {
        assertEquals("", OnlineSchemaChangeContextHolder.get(""));
    }

    @Test
    public void testGetValidKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }
}