package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithValidInput() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));

        SecurityContextUtils securityContextUtils = Mockito.mock(SecurityContextUtils.class);
        OnlineSchemaChangeContextHolder holder = new OnlineSchemaChangeContextHolder();
        holder.trace(userId, taskId, organizationId);
        holder.trace(SecurityContextUtils.getCurrentUser(), null, null);
        holder.retrace(context);
        Mockito.verify(securityContextUtils).setCurrentUser(eq(userId), eq(organizationId), any());
    }

    @Test
    public void testTraceWithNullInput() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder holder = new OnlineSchemaChangeContextHolder();
        holder.trace(null, null, null);
        holder.trace(null, null, null);
        holder.retrace(new HashMap<>());
    }

    @Test
    public void testGet() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId.toString());
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId.toString());
        context.put(OdcConstants.ORGANIZATION_ID, organizationId.toString());

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder holder = new OnlineSchemaChangeContextHolder();
        holder.trace(userId, taskId, organizationId);
        holder.trace(SecurityContextUtils.getCurrentUser(), null, null);
        holder.retrace(context);
        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder holder = new OnlineSchemaChangeContextHolder();
        holder.trace(userId, taskId, organizationId);
        holder.clear();
    }

}