package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;
    private Map<String, String> context;

    @Before
    public void setUp() {
        onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
        context = new HashMap<>();
    }

    @Test
    public void testTraceWithLongArguments() {
        long userId = 1L;
        long taskId = 2L;
        long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", OnlineSchemaChangeHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        Mockito.verify(MDC::put).withArguments(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId + "");
        Mockito.verify(MDC::put).withArguments(OnlineSchemaChangeContextHolder.TASK_ID, taskId + "");
        Mockito.verify(MDC::put).withArguments(OdcConstants.ORGANIZATION_ID, organizationId + "");

        OnlineSchemaChangeContextHolder.retrace(context);
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArguments() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        Mockito.verify(MDC::put).withArguments(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId);
        Mockito.verify(MDC::put).withArguments(OnlineSchemaChangeContextHolder.TASK_ID, taskId);
        Mockito.verify(MDC::put).withArguments(OdcConstants.ORGANIZATION_ID, organizationId);

        OnlineSchemaChangeContextHolder.retrace(context);
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        long userId = 1L;
        long taskId = 2L;
        long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}