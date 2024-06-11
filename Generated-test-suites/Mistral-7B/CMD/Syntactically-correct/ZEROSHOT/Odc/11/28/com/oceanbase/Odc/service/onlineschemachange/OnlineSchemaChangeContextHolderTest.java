package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.slf4j.helpers.MDCBase;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLongUserIdAndTaskId() {
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
    public void testTraceStringUserIdAndTaskId() {
        Long userId = 1L;
        String userIdString = "1";
        Long taskId = 2L;
        String taskIdString = "2";
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userIdString, taskIdString, organizationId.toString());

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullValues() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "userId1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskId1");
        context.put(OdcConstants.ORGANIZATION_ID, "organizationId1");

        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> currentContext = MDC.getCopyOfContextMap();
        assertEquals("userId1", currentContext.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("taskId1", currentContext.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("organizationId1", currentContext.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "userId1");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskId1");
        MDC.put(OdcConstants.ORGANIZATION_ID, "organizationId1");

        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "userId1");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskId1");
        MDC.put(OdcConstants.ORGANIZATION_ID, "organizationId1");

        assertEquals("userId1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("taskId1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("organizationId1", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        assertEquals(null, OnlineSchemaChangeContextHolder.get("nonExistentKey"));
    }

    @Test
    public void testSetCurrentUser() {
        Long userId = 1L;
        Long organizationId = 2L;

        SecurityContextUtils securityContextUtils = Mockito.mock(SecurityContextUtils.class);
        OnlineSchemaChangeContextHolder.trace(userId, null, organizationId);

        Mockito.verify(securityContextUtils).setCurrentUser(userId, organizationId, null);
    }
}

class MockitoJUnitRunner {}