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

        String userIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String organizationIdValue = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);

        assertEquals("" + userId, userIdValue);
        assertEquals("" + taskId, taskIdValue);
        assertEquals("" + organizationId, organizationIdValue);
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String userIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String organizationIdValue = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);

        assertEquals(userId, userIdValue);
        assertEquals(taskId, taskIdValue);
        assertEquals(organizationId, organizationIdValue);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        String userIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);

        assertNull(userIdValue);
        assertNull(taskIdValue);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullKey() {
        OnlineSchemaChangeContextHolder.get(null);
    }

    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();

        String userIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String organizationIdValue = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);

        assertNull(userIdValue);
        assertNull(taskIdValue);
        assertNull(organizationIdValue);
    }

}