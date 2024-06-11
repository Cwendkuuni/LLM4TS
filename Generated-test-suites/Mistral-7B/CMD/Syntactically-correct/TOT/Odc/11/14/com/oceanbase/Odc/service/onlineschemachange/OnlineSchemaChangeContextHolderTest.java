package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.MDC;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Mock
    private SecurityContextUtils securityContextUtils;

    private OnlineSchemaChangeContextHolder holder;

    @Before
    public void setUp() {
        holder = new OnlineSchemaChangeContextHolder();
    }

    @Test
    public void testTraceWithLongValues() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        holder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringValues() {
        Long userId = 1L;
        String userIdStr = userId.toString();
        Long taskId = 2L;
        String taskIdStr = taskId.toString();
        Long organizationId = 3L;
        String organizationIdStr = organizationId.toString();

        holder.trace(userIdStr, taskIdStr, organizationIdStr);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertSame(userId, Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)));
        assertSame(taskId, Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_ID)));
        assertSame(organizationId, Long.parseLong(context.get(OdcConstants.ORGANIZATION_ID)));
    }

    @Test
    public void testTraceWithNullValues() {
        holder.trace(null, null, null);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testSetCurrentUser() {
        Long userId = 1L;
        Long organizationId = 2L;

        when(securityContextUtils.getCurrentUser()).thenReturn(userId);

        holder.trace(userId, null, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(userId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> originalContext = new HashMap<>();
        originalContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        originalContext.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        originalContext.put(OdcConstants.ORGANIZATION_ID, "3");

        holder.retrace(originalContext);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertSame(originalContext, context);
    }

    @Test
    public void testClear() {
        holder.trace(1L, 2L, 3L);
        holder.clear();

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        holder.trace(1L, 2L, 3L);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), context.get(OdcConstants.ORGANIZATION_ID));
    }
}