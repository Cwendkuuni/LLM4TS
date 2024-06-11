package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        // Initialize MDC before each test
        MDC.clear();
    }

    @After
    public void tearDown() {
        // Clear MDC after each test
        MDC.clear();
    }

    @Test
    public void testTraceWithLongArguments() {
        long userId = 1L;
        long taskId = 2L;
        long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArguments() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Long userIdL = (userId == null ? null : Long.parseLong(userId));
        Long taskIdL = (taskId == null ? null : Long.parseLong(taskId));
        Long organizationIdL = (organizationId == null ? null : Long.parseLong(organizationId));
        OnlineSchemaChangeContextHolder.trace(userIdL, taskIdL, organizationIdL);

        assertEquals(userIdL + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL + "", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testReTrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "testTaskId");
        context.put(OdcConstants.ORGANIZATION_ID, "testOrganizationId");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("testTaskId", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("testOrganizationId", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals(1L + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(2L + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(3L + "", MDC.get(OdcConstants.ORGANIZATION_ID));

        assertNull(MDC.get(null));
    }
}