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
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId = OnlineSchemaChangeChangeHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals("2", taskId);
        assertEquals("3", organizationId);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithStringParameters() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        Long userId = Long.parseLong("1");
        Long taskIdL = Long.parseLong("2");
        Long organizationIdL = Long.parseLong("3");
        assertEquals("2", taskId);
        assertEquals("3", organizationId);
        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithNullParameters() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(null, taskId);
        assertEquals(null, organizationId);
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");
        context.put(OdcConstants.ORGANIZATION_ID, "2");
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        String taskId1 = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId1 = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals("1", taskId1);
        assertEquals("2", organizationId1);
        OnlineSchemaChangeContextHolder.retrace(context);
        String taskId2 = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId2 = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(null, taskId2);
        assertEquals(null, organizationId2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
    }

    @Test
    public void testGetNullKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertNull(OnlineSchemaChangeContextHolder.get(null));
        OnlineSchemaChangeContextHolder.clear();
    }

}