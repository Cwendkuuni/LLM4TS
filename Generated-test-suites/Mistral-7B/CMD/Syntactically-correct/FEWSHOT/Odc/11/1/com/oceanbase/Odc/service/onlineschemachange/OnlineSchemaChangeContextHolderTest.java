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

    private OnlineSchemaChangeContextHolder holder;
    private Long userId;
    private Long organizationId;
    private Long taskId;

    @Before
    public void setUp() {
        holder = new OnlineSchemaChangeContextHolder();
        userId = 1L;
        organizationId = 2L;
        taskId = 3L;
    }

    @Test
    public void testTraceWithLongArgs() {
        holder.trace(userId, taskId, organizationId);
        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArgs() {
        String userIdStr = userId.toString();
        String organizationIdStr = organizationId.toString();
        String taskIdStr = taskId.toString();

        holder.trace(userIdStr, taskIdStr, organizationIdStr);

        assertEquals(userIdStr, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdStr, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdStr, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        holder.trace(userId, taskId, organizationId);
        holder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        holder.trace(userId, taskId, organizationId);
        assertEquals(userId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), holder.get(OdcConstants.ORGANIZATION_ID));
        assertNull(holder.get("nonExistentKey"));
    }

    @Test
    public void testSetContextMap() {
        Map<String, String> context = new HashMap<>();
        holder.retrace(context);
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "workspaceValue");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskIdValue");
        MDC.put(OdcConstants.ORGANIZATION_ID, "organizationIdValue");

        holder.trace(userId, taskId, organizationId);

        Map<String, String> retrievedContext = new HashMap<>();
        holder.retrace(retrievedContext);

        assertEquals("workspaceValue", retrievedContext.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("taskIdValue", retrievedContext.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("organizationIdValue", retrievedContext.get(OdcConstants.ORGANIZATION_ID));
    }
}