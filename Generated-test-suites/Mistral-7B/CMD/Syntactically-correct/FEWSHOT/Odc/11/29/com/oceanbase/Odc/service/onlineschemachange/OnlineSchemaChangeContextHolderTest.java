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
    public void testTraceWithLongValues() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringValues() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getContextMap();
        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);
        assertEquals(userIdL.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullValues() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        Map<String, String> context = MDC.getContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testReTrace() {
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

        Map<String, String> context = MDC.getContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        String value = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals("1", value);
    }

    @Test
    public void testGetWithNullKey() {
        OnlineSchemaChangeContextHolder.get(null);

        Map<String, String> context = MDC.getContextMap();
        assertNull(context.get(null));
    }

    @Test
    public void testGetWithEmptyContext() {
        Map<String, String> context = new HashMap<>();
        MDC.setContextMap(context);

        String value = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertNull(value);
    }

    @Test
    public void testSetCurrentUserWithLongValues() {
        Long userId = 1L;
        Long organizationId = 2L;

        SecurityContextUtils securityContextUtils = mock(SecurityContextUtils.class);
        when(securityContextUtils.setCurrentUser(userId, organizationId, null)).thenReturn(null);

        OnlineSchemaChangeContextHolder.trace(userId, null, organizationId);
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        OnlineSchemaChangeContextHolder.retrace(MDC.getContextMap());

        assertEquals(userId, SecurityContextUtils.getCurrentUserId());
        assertEquals(organizationId, SecurityContextUtils.getCurrentOrganizationId());
    }

    @Test
    public void testSetCurrentUserWithStringValues() {
        String userId = "1";
        String organizationId = "2";

        SecurityContextUtils securityContextUtils = mock(SecurityContextUtils.class);
        when(securityContextUtils.setCurrentUser(Long.parseLong(userId), Long.parseLong(organizationId), null)).thenReturn(null);

        OnlineSchemaChangeContextHolder.trace(userId, null, organizationId);
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        OnlineSchemaChangeContextHolder.retrace(MDC.getContextMap());

        assertEquals(Long.parseLong(userId), SecurityContextUtils.getCurrentUserId());
        assertEquals(Long.parseLong(organizationId), SecurityContextUtils.getCurrentOrganizationId());
    }
}