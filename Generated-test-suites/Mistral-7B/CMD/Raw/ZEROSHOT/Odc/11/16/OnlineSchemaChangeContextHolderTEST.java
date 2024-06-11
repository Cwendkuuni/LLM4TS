package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLongUserId() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId + "", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", context.get(OdcConstants.ORGANIZATION_ID));

        SecurityContextUtils securityContextUtilsMock = Mockito.mock(SecurityContextUtils.class);
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Mockito.verify(securityContextUtilsMock).setCurrentUser(userId, organizationId, null);
    }

    @Test
    public void testTraceStringUserId() {
        Long userId = 1L;
        String userIdStr = "1";
        String taskId = "2";
        String organizationIdStr = "3";

        OnlineSchemaChangeContextHolder.trace(userIdStr, taskId, organizationIdStr);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId + "", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> contextAfterRetrace = MDC.getCopyOfContextMap();
        assertEquals(context, contextAfterRetrace);
    }

    @Test
    public void testClear() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        OnlineSchemaChangeContextHolder.clear();

        assertTrue(MDC.isEmpty());
    }

    @Test
    public void testGet() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}