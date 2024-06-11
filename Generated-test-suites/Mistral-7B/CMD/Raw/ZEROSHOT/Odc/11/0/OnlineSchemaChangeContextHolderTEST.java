package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder contextHolder;

    @Before
    public void setup() {
        contextHolder = new OnlineSchemaChangeContextHolder();
    }

    @Test
    public void testTraceWithLongParameters() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        contextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = contextHolder.getAllContext();
        assertEquals(3, context.size());
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        contextHolder.trace(userId + "", taskId + "", organizationId + "");

        Map<String, String> context = contextHolder.getAllContext();
        assertEquals(3, context.size());
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId.toString());
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId.toString());
        context.put(OdcConstants.ORGANIZATION_ID, organizationId.toString());

        contextHolder.trace(userId, taskId, organizationId);
        contextHolder.retrace(context);

        Map<String, String> newContext = contextHolder.getAllContext();
        assertEquals(Collections.emptyMap(), newContext);
    }

    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        contextHolder.trace(userId, taskId, organizationId);
        contextHolder.clear();

        Map<String, String> context = contextHolder.getAllContext();
        assertEquals(Collections.emptyMap(), context);
    }

    @Test
    public void testGet() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        contextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = contextHolder.getAllContext();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));

        assertEquals(userId.toString(), contextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), contextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), contextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGetNullContext() {
        assertNull(contextHolder.get(null));
    }

    @Test
    public void testSetContextMap() {
        Map<String, String> context = Mockito.mock(Map.class);
        contextHolder.retrace(context);

        Map<String, String> newContext = contextHolder.getAllContext();
        assertEquals(Collections.emptyMap(), newContext);
        Mockito.verify(context).clear();
    }
}