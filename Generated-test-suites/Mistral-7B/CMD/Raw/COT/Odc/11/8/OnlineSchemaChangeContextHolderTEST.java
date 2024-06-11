package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongUserIdAndLongTaskIdAndLongOrganizationId() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID);
        assertNotNull(userIdStr);
        assertNotNull(organizationIdStr);
        assertEquals(userId.toString(), userIdStr);
        assertEquals(organizationId.toString(), organizationIdStr);
        MDC.clear();
    }

    @Test
    public void testTraceWithStringUserIdAndStringTaskIdAndStringOrganizationId() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID);
        assertNotNull(userIdStr);
        assertNotNull(organizationIdStr);
        assertEquals(userId, userIdStr);
        assertEquals(organizationId, organizationIdStr);
        MDC.clear();
    }

    @Test
    public void testTraceWithNullUserIdAndNullTaskIdAndNullOrganizationId() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertTrue(MDC.isEmpty());
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        context.put(OnlineSchemaChangeChangeHolder.TASK_ID, "1");
        context.put(OnlineSchemaChangeContextHolder.ORGANIZATION_ID, "3");
        OnlineSchemaChangeContextHolder.retrace(context);
        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID);
        assertNotNull(userIdStr);
        assertNotNull(organizationIdStr);
        assertEquals("1", userIdStr);
        assertEquals("3", organizationIdStr);
        MDC.clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClearWhenMDCIsEmpty() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testGetWithNullKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        String result = OnlineSchemaChangeContextHolder.get(null);
        assertNull(result);
    }
}