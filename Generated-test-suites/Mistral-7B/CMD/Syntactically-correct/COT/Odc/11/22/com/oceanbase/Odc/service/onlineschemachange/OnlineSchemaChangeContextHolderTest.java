package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.MDC;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Mock
    private MDC mdc;

    @InjectMocks
    private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;

    @Before
    public void setup() {
        when(mdc.get(OnlineSchemaChangeContextHolder.TASK_ID)).thenReturn(null);
        when(mdc.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).thenReturn(null);
        when(mdc.get(OdcConstants.ORGANIZATION_ID)).thenReturn(null);
    }

    @Test
    public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceInvalidUserId() {
        onlineSchemaChangeContextHolder.trace("abc", 2L, 3L);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceInvalidTaskId() {
        Long userId = 1L;
        onlineSchemaChangeContextHolder.trace(userId, "abc", 3L);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceInvalidOrganizationId() {
        Long userId = 1L;
        Long taskId = 2L;
        onlineSchemaChangeContextHolder.trace(userId, taskId, "abc");
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        onlineSchemaChangeContextHolder.retrace(context);
        MDC.setContextMap(context);
        assertEquals(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(context.get(OnlineSchemaChangeContextHolder.TASK_ID), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(context.get(OdcConstants.ORGANIZATION_ID), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        onlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        onlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        onlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals(Long.toString(1), onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(Long.toString(2), onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(Long.toString(3), onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}