package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;
    private Map<String, String> context;

    @Before
    public void setup() {
        onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
        context = new HashMap<>();
    }

    @Test
    public void testTraceWithLongParameters() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));

        onlineSchemaChangeChangeHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        Long userId = 1L;
        String userIdStr = userId.toString();
        Long taskId = 2L;
        String taskIdStr = taskId.toString();
        Long organizationId = 3L;
        String organizationIdStr = organizationId.toString();

        onlineSchemaChangeContextHolder.trace(userIdStr, taskIdStr, organizationIdStr);

        assertEquals(userIdStr, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdStr, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdStr, onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        assertEquals(userIdStr, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdStr, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdStr, MDC.get(OdcConstants.ORGANIZATION_ID));

        onlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        when(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).thenReturn("1");
        when(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID)).thenReturn("2");
        when(MDC.get(OdcConstants.ORGANIZATION_ID)).thenReturn("3");

        onlineSchemaChangeContextHolder.retrace(context);

        assertEquals("1", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        when(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).thenReturn("1");
        when(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID)).thenReturn("2");
        when(MDC.get(OdcConstants.ORGANIZATION_ID)).thenReturn("3");

        onlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }
}