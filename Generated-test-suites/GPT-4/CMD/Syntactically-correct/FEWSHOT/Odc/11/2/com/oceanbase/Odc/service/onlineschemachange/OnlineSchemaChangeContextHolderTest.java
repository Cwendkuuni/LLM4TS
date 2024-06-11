package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongs() {
        Long userId = 1234567890L;
        Long taskId = 4567890123L;
        Long organizationId = 7890123456L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTraceWithStrings() {
        String userId = "1234567890";
        String taskId = "4567890123";
        String organizationId = "7890123456";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        MDC.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));

        String userId = "1234567890";
        String taskId = "4567890123";
        String organizationId = "7890123456";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertNotNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));

        MDC.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));

        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());

        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        Long userId = 1234567890L;
        Long taskId = 4567890123L;
        Long organizationId = 7890123456L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertNotNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNotNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNotNull(MDC.get("organizationId"));

        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get("organizationId"));
    }

    @After
    public void tearDown() {
        MDC.clear();
    }
}