package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongArgs() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals(userId.toString(), userIdStr);

        String taskIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskId.toString(), taskIdStr);

        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(organizationId.toString(), organizationIdStr);

        // Check current user is set
        Long currentUserId = SecurityContextUtils.getCurrentUser().getId();
        assertEquals(userId, currentUserId);
    }

    @Test
    public void testTraceWithStringArgs() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals(userId, userIdStr);

        String taskIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskId, taskIdStr);

        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(organizationId, organizationIdStr);

        // Check current user is set
        Long currentUserId = SecurityContextUtils.getCurrentUser().getId();
        assertEquals(Long.parseLong(userId), currentUserId);
    }

    @Test
    public void testRetrace() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId + "");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId + "");
        context.put(OdcConstants.ORGANIZATION_ID, organizationId + "");

        OnlineSchemaChangeContextHolder.retrace(context);

        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertNull(userIdStr);

        String taskIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertNull(taskIdStr);

        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertNull(organizationIdStr);

        // Check current user is not set
        Long currentUserId = SecurityContextUtils.getCurrentUser().getId();
        assertEquals(0, currentUserId);
    }

    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        OnlineSchemaChangeContextHolder.clear();

        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertNull(userIdStr);

        String taskIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertNull(taskIdStr);

        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertNull(organizationIdStr);

        // Check current user is not set
        Long currentUserId = SecurityContextUtils.getCurrentUser().getId();
        assertEquals(0, currentUserId);
    }
}