package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> context;
    private OnlineSchemaChangeContextHolder contextHolder;

    @Before
    public void setup() {
        context = new HashMap<>();
        contextHolder = new OnlineSchemaChangeContextHolder();
        MDC.setContextMap(context);
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongValues() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        contextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringValues() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        contextHolder.trace(userId, taskId, organizationId);

        assertSame(userId, Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)));
        assertSame(taskId, Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_ID)));
        assertSame(organizationId, Long.parseLong(context.get(OdcConstants.ORGANIZATION_ID)));
    }

    @Test
    public void testTraceWithNullValues() {
        contextHolder.trace(null, null, null);
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        contextHolder.trace(1L, 2L, 3L);
        Map<String, String> backupContext = new HashMap<>(context);
        contextHolder.retrace(context);

        assertEquals(backupContext, context);
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        contextHolder.trace(1L, 2L, 3L);
        contextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        contextHolder.trace(1L, 2L, 3L);

        assertEquals(1L, Long.parseLong(contextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)));
        assertEquals(2L, Long.parseLong(contextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID)));
        assertEquals(3L, Long.parseLong(contextHolder.get(OdcConstants.ORGANIZATION_ID)));
    }
}