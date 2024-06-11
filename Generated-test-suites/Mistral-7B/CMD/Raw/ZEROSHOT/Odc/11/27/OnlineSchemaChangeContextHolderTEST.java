package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;
    private Map<String, String> context;

    @Before
    public void setUp() {
        onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
        context = new HashMap<>();
    }

    @After
    public void tearDown() {
        onlineSchemaChangeContextHolder = null;
        context = null;
    }

    @Test
    public void testTraceWithLongParameters() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);

        assertEquals(userIdL.toString(), onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL.toString(), onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");

        onlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        onlineSchemaChangeChangeHolder.retrace(context);

        Map<String, String> expectedContext = new HashMap<>();
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        expectedContext.put(OdcConstants.ORGANIZATION_ID, "3");

        assertEquals(expectedContext, context);
    }

    @Test
    public void testClear() {
        onlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        onlineSchemaChangeContextHolder.clear();

        assertNull(onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}