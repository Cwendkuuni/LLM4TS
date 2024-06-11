package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.MDC;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @InjectMocks
    private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;

    @Test
    public void testTraceWithLongArgments() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContext();
        assertEquals("" + userId, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("" + taskId, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("" + organizationId, context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArgments() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContext();
        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);
        assertEquals("" + userIdL, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("" + taskIdL, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("" + organizationIdL, context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullArguments() {
        onlineSchemaChangeContextHolder.trace(null, null, null);

        Map<String, String> context = MDC.getCopyOfContext();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        onlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> contextAfterTrace = MDC.getCopyOfContext();
        assertEquals(context, contextAfterTrace);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClear() {
        onlineSchemaChangeContextHolder.clear();

        Map<String, String> contextBeforeClear = MDC.getCopyOfContext();
        onlineSchemaChangeContextHolder.clear();
        Map<String, String> contextAfterClear = MDC.getCopyOfContext();

        assertEquals(contextBeforeClear, contextAfterClear); // context should not be cleared
    }

    @Test
    public void testGet() {
        onlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals("1", onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        onlineSchemaChangeContextHolder.clear();

        assertNull(onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}