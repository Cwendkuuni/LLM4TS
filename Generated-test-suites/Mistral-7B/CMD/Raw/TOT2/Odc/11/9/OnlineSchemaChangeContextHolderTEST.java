
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithValidArguments() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = OnlineSchemaChangeContextHolder.getMDCContext();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OnlineSchemaChangeHolder.ODC_CONSTANTS_ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithValidStringArguments() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = OnlineSchemaChangeContextHolder.getMDCContext();
        assertEquals(userId, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, context.get(OnlineSchemaChangeHolder.ODC_CONSTANTS_ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OnlineSchemaChangeHolder.ODC_CONSTANTS_ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> currentContext = OnlineSchemaChangeContextHolder.getMDCContext();
        assertEquals(context, currentContext);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> context = OnlineSchemaChangeContextHolder.getMDCContext();
        assertEquals(null, context);
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeHolder.ODC_CONSTANTS_ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> context = OnlineSchemaChangeContextHolder.getMDCContext();
        assertEquals(null, context);
    }
}
