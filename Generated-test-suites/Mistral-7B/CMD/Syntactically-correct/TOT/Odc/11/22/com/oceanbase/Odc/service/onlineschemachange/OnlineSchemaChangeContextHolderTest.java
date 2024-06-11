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

    private OnlineSchemaChangeContextHolder contextHolder;
    private SecurityContextUtils securityContextUtils;
    private Long userId = 1L;
    private Long taskId = 1L;
    private Long organizationId = 1L;
    private Map<String, String> context;

    @Before
    public void setup() {
        contextHolder = new OnlineSchemaChangeContextHolder();
        securityContextUtils = mock(SecurityContextUtils.class);
    }

    @Test
    public void testTraceWithLongValues() {
        when(securityContextUtils.getCurrentUser()).thenReturn(userId);

        contextHolder.trace(userId, taskId, organizationId);
        assertEquals(userId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", MDC.get(OnlineSchemaChangeHolder.TASK_ID));
        assertEquals(organizationId + "", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringValues() {
        String userIdStr = userId + "";
        String taskIdStr = taskId + "";
        String organizationIdStr = organizationId + "";

        when(securityContextUtils.getCurrentUser()).thenReturn(userIdStr);

        contextHolder.trace(userIdStr, taskIdStr, organizationIdStr);
        assertEquals(userIdStr, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdStr, MDC.get(OnlineSchemaChangeHolder.TASK_ID));
        assertEquals(organizationIdStr, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId + "");
        context.put(OdcConstants.ORGANIZATION_ID, organizationId + "");

        contextHolder.retrace(context);
        assertEquals(context, MDC.getContextMap());
    }

    @Test
    public void testClear() {
        contextHolder.trace(userId, taskId, organizationId);
        contextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        contextHolder.trace(userId, taskId, organizationId);
        assertEquals(taskId + "", contextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
}