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

        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArgs() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        MDC.clear();

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);

        assertEquals(userIdL.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullArgs() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        MDC.setContextMap(context);

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(context, MDC.getCopyOfContextMap());
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "workspace");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskId");
        MDC.put(OdcConstants.ORGANIZATION_ID, "organizationId");

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testSetCurrentUser() {
        Long userId = 1L;
        Long organizationId = 2L;

        MDC.clear();

        OnlineSchemaChangeContextHolder.trace(userId, null, organizationId);

        SecurityContextUtils securityContextUtils = mock(SecurityContextUtils.class);
        when(securityContextUtils.getCurrentUser()).thenReturn(userId);

        OnlineSchemaChangeContextHolder.setCurrentUser(securityContextUtils);

        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}