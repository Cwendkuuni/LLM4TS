package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

public class OnlineSchemaChangeContextHolderTest {

    private static final String TASK_ID = "taskId";
    private static final String TASK_WORK_SPACE = "onlineSchemaChangeWorkSpace";
    private static final String ORGANIZATION_ID = "organizationId";

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        Long userId = 123L;
        Long taskId = 456L;
        Long organizationId = 789L;
        
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        
        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        String userId = "123";
        String taskId = "456";
        String organizationId = "789";
        
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        
        assertEquals(userId, OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        String userId = "123";
        String taskId = "456";
        String organizationId = "789";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        MDC.clear();
        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());

        assertEquals(userId, OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        String key = "key";
        String value = "value";

        MDC.put(key, value);

        assertEquals(value, OnlineSchemaChangeContextHolder.get(key));
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }
}