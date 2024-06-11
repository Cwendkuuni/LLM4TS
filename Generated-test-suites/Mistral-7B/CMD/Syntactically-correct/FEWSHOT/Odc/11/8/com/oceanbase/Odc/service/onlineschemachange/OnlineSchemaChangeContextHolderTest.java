package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongArgs() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        MDC.clear();

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));

        context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArgs() {
        Long userId = 1L;
        String userStr = userId.toString();
        Long taskId = 2L;
        String taskStr = taskId.toString();
        Long organizationId = 3L;
        String organizationStr = organizationId.toString();

        MDC.clear();

        OnlineSchemaChangeContextHolder.trace(userStr, taskStr, organizationStr);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userStr, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskStr, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationStr, context.get(OdcConstants.ORGANIZATION_ID));

        context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullArgs() {
        MDC.clear();

        OnlineSchemaChangeContextHolder.trace(null, null, null);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> currentContext = MDC.getCopyOfContextMap();
        assertEquals(context, currentContext);

        context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        currentContext = MDC.getCopyOfContextMap();
        assertNull(currentContext);
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskId");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "userId");
        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskId");
        String result = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals("taskId", result);

        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, null);
        result = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertNull(result);
    }

    @Test
    public void testGetWithNullKey() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskId");
        String result = OnlineSchemaChangeContextHolder.get(null);
        assertNull(result);
    }

    @Test
    public void testGetWithEmptyStringKey() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskId");
        String result = OnlineSchemaChangeContextHolder.get("");
        assertNull(result);
    }

    @Test
    public void testSetContextUtils() {
        SecurityContextUtils securityContextUtils = mock(SecurityContextUtils.class);
        when(securityContextUtils.getCurrentUser()).thenReturn(1L);

        MDC.clear();

        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        OnlineSchemaChangeContextHolder.trace("userId1", "taskId1", "organizationId1");

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(Long.toString(1), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(Long.toString(2), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(Long.toString(3), context.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.retrace(context);

        when(securityContextUtils.getCurrentUser()).thenReturn(2L);

        OnlineSchemaChangeContextHolder.trace("userId2", "taskId2", "organizationId2");

        context = MDC.getCopyOfContextMap();
        assertEquals(Long.toString(2), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(Long.toString(2), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(Long.toString(3), context.get(OdcConstants.ORGANIZATION_ID));

        assertEquals(1L, securityContextUtils.getCurrentUser().longValue());
    }
}