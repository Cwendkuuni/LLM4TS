package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    private Long userId;
    private Long taskId;
    private Long organizationId;
    private Map<String, String> contextMap;

    @Before
    public void setUp() {
        this.userId = 1L;
        this.taskId = 2L;
        this.organizationId = 3L;
        contextMap = new HashMap<String, String>();
        contextMap.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, String.valueOf(userId));
        contextMap.put(OnlineSchemaChangeContextHolder.TASK_ID, String.valueOf(taskId));
        contextMap.put(OdcConstants.ORGANIZATION_ID, String.valueOf(organizationId));
    }

    @Test
    public void testTraceWithLongs() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(String.valueOf(userId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(String.valueOf(taskId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(String.valueOf(organizationId), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStrings() {
        OnlineSchemaChangeContextHolder.trace(String.valueOf(userId), String.valueOf(taskId), String.valueOf(organizationId));

        assertEquals(String.valueOf(userId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(String.valueOf(taskId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(String.valueOf(organizationId), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(contextMap);

        assertEquals(String.valueOf(userId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(String.valueOf(taskId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(String.valueOf(organizationId), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(String.valueOf(userId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(String.valueOf(taskId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(String.valueOf(organizationId), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

}