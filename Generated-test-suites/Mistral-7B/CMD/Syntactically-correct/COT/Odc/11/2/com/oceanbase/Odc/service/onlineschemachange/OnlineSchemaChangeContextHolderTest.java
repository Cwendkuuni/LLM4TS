package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.MDC;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongArguments() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdStr = OnlineSchemaChangeChangeHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(userId + "", userIdStr);
        assertEquals(organizationId + "", organizationIdStr);
    }

    @Test
    public void testTraceWithStringArguments() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Long userIdL = (userId == null ? null : Long.parseLong(userId));
        Long taskIdL = (taskId == null ? null : Long.parseLong(taskId));
        Long organizationIdL = (organizationId == null ? null : Long.parseLong(organizationId));
        assertEquals(userIdL + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL + "", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullArguments() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidLongArguments() {
        OnlineSchemaChangeContextHolder.trace("invalidUserId", "taskId", "organizationId");
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = MDC.getCopyOfContextMap();
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(MDC.get(OdcConstants.ORGANIZATION_ID), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        String userIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdStr = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(userId + "", userIdStr);
        assertEquals(organizationId + "", organizationIdStr);
    }

    @Test
    public void testGetWithNullKey() {
        assertNull(OnlineSchemaChangeContextHolder.get(null));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}