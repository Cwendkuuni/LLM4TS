package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = OnlineSchemaChangeContextHolder.getMDCContext();
        assertEquals("" + userId, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("" + organizationId, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("" + organizationId, context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = OnlineSchemaChangeContextHolder.getMDCContext();
        assertEquals("1", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("" + userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("" + organizationId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("" + organizationId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
        assertNull(OnlineSchemaChangeContextHolder.get("non_existing_key"));
    }

    @Test
    public void testTraceEdgeCases() {
        Long userId = Long.MAX_VALUE;
        Long taskId = -1L;
        String organizationId = new String(new char[1025]) {};

        try {
            OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // ... Add more test cases for other edge cases
    }

    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> context = OnlineSchemaChangeContextHolder.getMDCContext();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = OnlineSchemaChangeContextHolder.getMDCContext();
        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> newContext = OnlineSchemaChangeContextHolder.getMDCContext();
        assertEquals(context, newContext);
    }
}