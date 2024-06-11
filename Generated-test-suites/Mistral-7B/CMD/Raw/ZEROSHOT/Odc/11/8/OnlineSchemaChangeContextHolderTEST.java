package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        context.clear();
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

        assertEquals(userId, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> oldContext = new HashMap<>();
        oldContext.put("oldKey", "oldValue");

        MDC.setContextMap(oldContext);
        onlineSchemaChangeContextHolder.retrace(oldContext);

        assertEquals(oldContext, MDC.getContextMap());
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "workSpaceValue");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskIdValue");
        MDC.put(OdcConstants.ORGANIZATION_ID, "organizationIdValue");

        onlineSchemaChangeContextHolder.clear();

        assertNull(onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "workSpaceValue");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "taskIdValue");
        MDC.put(OdcConstants.ORGANIZATION_ID, "organizationIdValue");

        assertEquals("workSpaceValue", onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("taskIdValue", onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("organizationIdValue", onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        assertNull(onlineSchemaChangeContextHolder.get("nonExistentKey"));
    }

    @Test
    public void testTraceWithNullParameters() {
        onlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}