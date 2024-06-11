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
        MDC.clear(); // Clear any existing context before each test
    }

    @After
    public void tearDown() {
        MDC.clear(); // Clear any context set during the test
    }

    @Test
    public void testTraceWithValidArguments() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> expectedContext = new HashMap<>();
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId.toString());
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId.toString());
        expectedContext.put(OdcConstants.ORGANIZATION_ID, organizationId.toString());

        Map<String, String> actualContext = MDC.getCopyOfContextMap();

        assertEquals(expectedContext, actualContext);
    }

    @Test
    public void testTraceWithNullArguments() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        Map<String, String> actualContext = MDC.getCopyOfContextMap();

        assertTrue(actualContext.isEmpty());
    }

    @Test
    public void testTraceWithStringArguments() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> expectedContext = new HashMap<>();
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId);
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId);
        expectedContext.put(OdcConstants.ORGANIZATION_ID, organizationId);

        Map<String, String> actualContext = MDC.getCopyOfContextMap();

        assertEquals(expectedContext, actualContext);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> actualContext = MDC.getCopyOfContextMap();

        assertEquals(context, actualContext);
    }

    @Test
    public void testClear() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(context);

        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> actualContext = MDC.getCopyOfContextMap();

        assertTrue(actualContext.isEmpty());
    }

    @Test
    public void testGet() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(context);

        String actualValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);

        assertEquals("2", actualValue);
    }
}