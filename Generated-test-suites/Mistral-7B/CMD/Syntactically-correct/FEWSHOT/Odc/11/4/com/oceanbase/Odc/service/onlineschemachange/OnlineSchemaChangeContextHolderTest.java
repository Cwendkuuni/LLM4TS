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
    public void testTraceLongUserId() {
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
    public void testTraceStringUserId() {
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
    public void testTraceNullValues() {
        MDC.clear();

        OnlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithCurrentUser() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        MDC.clear();

        // Mock SecurityContextUtils.setCurrentUser()
        SecurityContextUtils securityContextUtils = mock(SecurityContextUtils.class);
        when(securityContextUtils.setCurrentUser(userId, organizationId, null)).thenReturn(true);

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.retrace(MDC.getContextMap());

        // Verify SecurityContextUtils.setCurrentUser() was called
        assertEquals(userId, securityContextUtils.getCurrentUser().getId());
        assertEquals(organizationId, securityContextUtils.getCurrentUser().getOrganizationId());
        assertNull(securityContextUtils.getCurrentUser().getTenantId());
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "userId1");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskId1");
        MDC.put(OdcConstants.ORGANIZATION_ID, "organizationId1");

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGetKey() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "userId1");

        assertEquals("userId1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testNullKey() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "userId1");

        assertNull(OnlineSchemaChangeContextHolder.get(null));
    }

    @Test
    public void testGetEmptyMap() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "userId1");

        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("userId1", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, context.get(OdcConstants.ORGANIZATION_ID));
    }
}