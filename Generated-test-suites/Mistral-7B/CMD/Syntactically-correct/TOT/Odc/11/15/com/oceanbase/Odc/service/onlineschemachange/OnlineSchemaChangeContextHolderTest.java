package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder holder;
    private Map<String, String> context;

    @Before
    public void setup() {
        holder = new OnlineSchemaChangeContextHolder();
        context = new HashMap<>();
        MDC.setContextMap(context);
    }

    @After
    public void tearDown() {
        holder.clear();
    }

    @Test
    public void testTraceLongUserId() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        holder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), holder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringUserId() {
        Long userId = 1L;
        String userIdStr = userId.toString();
        Long taskId = 2L;
        String taskIdStr = taskId.toString();
        String organizationIdStr = "3";
        holder.trace(userIdStr, taskIdStr, organizationIdStr);

        assertEquals(userIdStr, holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdStr, holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdStr, holder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceNullValues() {
        holder.trace(null, null, null);
        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(holder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        holder.trace(userId, taskId, organizationId);

        Map<String, String> contextBackup = new HashMap<>(context);
        holder.retrace(context);

        assertEquals(contextBackup.toString(), context.toString());
    }

    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        holder.trace(userId, taskId, organizationId);

        holder.clear();

        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(holder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGetNullKey() {
        assertNull(holder.get(null));
    }
}