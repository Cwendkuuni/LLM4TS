package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongParams() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID);

        assertEquals(userId + "", userIdStr);
        assertEquals(organizationId + "", organizationIdStr);

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithStringParams() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Long userIdL = (userId == null ? null : Long.parseLong(userId));
        Long taskIdL = (taskId == null ? null : Long.parseLong(taskId));
        Long organizationIdL = (organizationId == null ? null : Long.parseLong(organizationId));

        assertEquals(userIdL + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidStringParams() {
        String userId = "a";
        String taskId = "b";
        String organizationId = "c";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test
    public void testRetrace() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeChangeContextHolder.retrace(context);

        assertEquals(userId + "", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        OnlineSchemaChangeContextHolder.clear();

        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }
}