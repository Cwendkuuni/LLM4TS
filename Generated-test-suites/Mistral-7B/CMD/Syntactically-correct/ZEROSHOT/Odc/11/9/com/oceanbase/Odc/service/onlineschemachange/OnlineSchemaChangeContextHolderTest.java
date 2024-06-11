package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceUserIdLong() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));

        // set current user to get tenant list
        SecurityContextUtils securityContextUtils = Mockito.mock(SecurityContextUtils.class);
        Mockito.when(securityContextUtils.getCurrentUser()).thenReturn(null);
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Mockito.verify(securityContextUtils).setCurrentUser(userId, organizationId, null);

        MDC.clear();
    }

    @Test
    public void testTraceUserIdString() {
        Long userId = 1L;
        String userIdStr = userId.toString();

        OnlineSchemaChangeContextHolder.trace(userIdStr, "taskId", "organizationId");

        assertEquals(userIdStr, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));

        MDC.clear();
    }

    @Test
    public void testTraceNull() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));

        MDC.clear();
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskId");
        context.put(OdcConstants.ORGANIZATION_ID, "organizationId");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("taskId", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("organizationId", MDC.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
    }
}