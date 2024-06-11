package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

/**
 * Unit tests for OnlineSchemaChangeContextHolder class
 */
public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongParameters() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        // Check if context values are set correctly
        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Long userIdL = (userId == null ? null : Long.parseLong(userId));
        Long taskIdL = (taskId == null ? null : Long.parseLong(taskId));
        Long organizationIdL = (organizationId == null ? null : Long.parseLong(organizationId));

        // Check if context values are set correctly
        assertEquals(userIdL.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeChangeHolder.retrace(context);

        // Check if context values are cleared after retracing
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        // Check if previously set context values are present in the retraced context
        assertEquals(1L, Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)));
        assertEquals(2L, Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_ID)));
        assertEquals(3L, Long.parseLong(context.get(OdcConstants.ORGANIZATION_ID)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTraceWithNullOrganizationId() {
        Long userId = 1L;
        Long taskId = 2L;
        String organizationId = null;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidUserId() {
        Long userId = null;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidTaskId() {
        Long userId = 1L;
        String taskId = null;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test
    public void testGet() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        // Check if context values can be retrieved
        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        // Check if context values are null when not set
        assertNull(OnlineSchemaChangeContextHolder.get(null));
    }

    @Test(expected = IllegalStateException.class)
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L); // Set context values
        OnlineSchemaChangeContextHolder.clear(); // Clear context

        // Check if context values are cleared
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        // Check if attempting to clear context when it's already cleared throws an exception
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testGetWhenContextIsCleared() {
        OnlineSchemaChangeContextHolder.clear();

        // Check if context values are null when context is cleared
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}