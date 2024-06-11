package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        // Initialize MDC before each test method
        MDC.clear();
    }

    @After
    public void tearDown() {
        // Clear MDC after each test method
        MDC.clear();
    }

    @Test
    public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Long userIdL = (userId == null ? null : Long.parseLong(userId));
        Long taskIdL = (taskId == null ? null : Long.parseLong(taskId));
        Long organizationIdL = (organizationId == null ? null : Long.parseLong(organizationId));
        OnlineSchemaChangeContextHolder.trace(userIdL, taskIdL, organizationIdL);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "testUserId");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "testTaskId");
        context.put(OdcConstants.ORGANIZATION_ID, "testOrganizationId");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("testUserId", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("testTaskId", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("testOrganizationId", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();

        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

}