package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;

import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongValues() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        MDC mdc = mock(MDC.class);
        when(mdc.put(eq(TASK_WORK_SPACE), eq(userId.toString()))).thenReturn(userId.toString());
        when(mdc.put(eq(TASK_ID), eq(taskId.toString()))).thenReturn(taskId.toString());
        when(mdc.put(eq(OdcConstants.ORGANIZATION_ID), eq(organizationId.toString()))).thenReturn(organizationId.toString());

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringValues() {
        String userId = "user1";
        String taskId = "task1";
        String organizationId = "org1";

        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);

        OnlineSchemaChangeContextHolder.trace(userIdL, taskIdL, organizationIdL);

        assertEquals(userId, MDC.get(TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWhenInputsAreNull() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(MDC.get(TASK_WORK_SPACE));
        assertNull(MDC.get(TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testSetContextMap() {
        Map<String, String> context = new HashMap<>();
        context.put(TASK_WORK_SPACE, "user1");
        context.put(TASK_ID, "task1");
        context.put(OdcConstants.ORGANIZATION_ID, "org1");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(context, MDC.getContextMap());
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(TASK_WORK_SPACE));
        assertNull(MDC.get(TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}