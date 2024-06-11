package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongArguments() {
        Map<String, String> context = new HashMap<>();
        MDC.setContextMap(context);

        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        // check current user was set
        SecurityContextUtils securityContextUtils = mock(SecurityContextUtils.class);
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        when(securityContextUtils.getCurrentUser()).thenReturn("test user");
        OnlineSchemaChangeContextHolder.trace(new Long[] {userId, taskId, organizationId}, securityContextUtils);
        assertEquals("test user", OnlineSchemaChangeContextHolder.get(SecurityContextUtils.CURRENT_USER));
    }

    @Test
    public void testTraceWithStringArguments() {
        Map<String, String> context = new HashMap<>();
        MDC.setContextMap(context);

        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals( userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE) );
        assertEquals( taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID) );
        assertEquals( organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID) );
    }

    @Test
    public void testClear() {
        Map<String, String> context = new HashMap<>();
        MDC.setContextMap(context);

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        Map<String, String> context = new HashMap<>();
        MDC.setContextMap(context);

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        assertNull(OnlineSchemaChangeContextHolder.get("unknownKey"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        MDC.setContextMap(context);

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(context, MDC.getCopyOfContextMap());
    }
}