package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongParameters() {
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
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = MDC.getCopyOfContextMap();
        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);
        assertEquals(userIdL.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullParameters() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(context, MDC.getCopyOfContextMap());
    }

    @Test(expected = IllegalStateException.class)
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        String value = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(value, null);
        // This line should throw an exception
        OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
    }

}