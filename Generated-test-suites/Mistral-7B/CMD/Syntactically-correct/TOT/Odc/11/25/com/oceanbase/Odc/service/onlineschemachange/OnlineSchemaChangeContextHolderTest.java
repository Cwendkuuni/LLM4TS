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
        // Clear MDC context before each test
        MDC.clear();
    }

    @After
    public void tearDown() {
        // Clear MDC context after each test
        MDC.clear();
    }

    @Test
    public void testTraceWithValidArgs() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskId + "", userIdStr);

        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeConstants.ORGANIZATION_ID);
        assertEquals(organizationId + "", organizationIdStr);
    }

    @Test
    public void testTraceWithNullArgs() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertNull(userIdStr);

        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeConstants.ORGANIZATION_ID);
        assertNull(organizationIdStr);
    }

    @Test
    public void testTraceWithValidStrings() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskId, userIdStr);

        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeConstants.ORGANIZATION_ID);
        assertEquals(organizationId, organizationIdStr);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");
        context.put(OnlineSchemaChangeConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals("1", userIdStr);

        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeConstants.ORGANIZATION_ID);
        assertEquals("3", organizationIdStr);
    }

    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(3, MDC.size());

        OnlineSchemaChangeContextHolder.clear();

        assertEquals(0, MDC.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidKey() {
        OnlineSchemaChangeContextHolder.get("invalidKey");
    }
}