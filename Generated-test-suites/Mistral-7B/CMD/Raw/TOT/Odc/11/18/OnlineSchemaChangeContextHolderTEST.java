package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongInput() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringInput() {
        Long userId = 1L;
        String userIdStr = userId.toString();

        Long taskId = 2L;
        String taskIdStr = taskId.toString();

        Long organizationId = 3L;
        String organizationIdStr = organizationId.toString();

        OnlineSchemaChangeContextHolder.trace(userIdStr, taskIdStr, organizationIdStr);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userIdStr, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdStr, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdStr, context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidInput() {
        Long userId = null;
        Long taskId = "invalid";
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test
    public void testRetrace() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId.toString());
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId.toString());
        context.put(OdcConstants.ORGANIZATION_ID, organizationId.toString());

        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> contextAfterRetrace = MDC.getCopyOfContextMap();
        assertTrue(context.equals(contextAfterRetrace));
    }

    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> contextAfterClear = MDC.getCopyOfContextMap();
        assertTrue(contextAfterClear.isEmpty());
    }

    @Test
    public void testGet() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskId.toString(), taskIdValue);
    }

    @After
    public void teardown() {
        MDC.clear();
    }
}