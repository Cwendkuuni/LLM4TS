package com.oceanbase.odc.service.onlineschemachange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;
    private Map<String, String> context;

    @Before
    public void setUp() {
        onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
        context = new HashMap<>();
        MDC.setContextMap(context);
    }

    @After
    public void tearDown() {
        onlineSchemaChangeContextHolder.clear();
        MDC.clear();
    }

    @Test
    public void testTraceWithLongParameters() {
        long userId = 1L;
        long taskId = 2L;
        long organizationId = 3L;

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("" + userId, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("" + taskId, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("" + organizationId, onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Long userIdLong = Long.parseLong(userId);
        Long taskIdLong = Long.parseLong(taskId);
        Long organizationIdLong = Long.parseLong(organizationId);

        assertEquals("" + userIdLong, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("" + taskIdLong, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("" + organizationIdLong, onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        long userId = 1L;
        long taskId = 2L;
        long organizationId = 3L;

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> contextBackup = new HashMap<>(MDC.getCopyOfContextMap());
        onlineSchemaChangeChangeContextHolder.retrace(context);

        assertEquals(contextBackup, MDC.getCopyOfContextMap());
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        long userId = 1L;
        long taskId = 2L;
        long organizationId = 3L;

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        onlineSchemaChangeContextHolder.clear();

        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        long userId = 1L;
        long taskId = 2L;
        long organizationId = 3L;

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        onlineSchemaChangeContextHolder.clear();

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("" + userId, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("" + taskId, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("" + organizationId, onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullParameters() {
        onlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}