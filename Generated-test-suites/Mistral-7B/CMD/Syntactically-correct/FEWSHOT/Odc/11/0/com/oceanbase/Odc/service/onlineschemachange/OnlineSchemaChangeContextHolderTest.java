package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongArguments() {
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
    public void testTraceWithStringArguments() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);
        assertEquals(userIdL.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullArguments() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        MDC.put(OdcConstants.ORGANIZATION_ID, "3");

        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("1", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        MDC.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        String taskWorkspace = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals("1", taskWorkspace);

        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");

        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals("2", taskId);

        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertNull(organizationId);
    }

    @Test
    public void testGetWithNullKey() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        String nullKey = null;
        String value = OnlineSchemaChangeContextHolder.get(nullKey);
        assertNull(value);
    }

    @Test
    public void testSetCurrentUser() {
        Long userId = 1L;
        Long organizationId = 2L;

        SecurityContextUtils securityContextUtils = mock(SecurityContextUtils.class);
        when(securityContextUtils.getCurrentUser()).thenReturn(userId);
        when(securityContextUtils.getOrganizationId(userId)).thenReturn(organizationId);

        OnlineSchemaChangeContextHolder.trace(securityContextUtils);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(userId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }
}