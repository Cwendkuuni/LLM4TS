
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    private final Long userId = 123L;
    private final Long taskId = 456L;
    private final Long organizationId = 789L;

    @Before
    public void setUp() throws Exception {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @After
    public void tearDown() throws Exception {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.clear();
        OnlineSchemaChangeContextHolder.trace(userId.toString(), taskId.toString(), organizationId.toString());

        testTraceLong();
    }

    @Test
    public void testRetrace() {
        Map<String, String> newContext = new HashMap<>();
        newContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "newTaskWorkSpace");
        newContext.put(OnlineSchemaChangeContextHolder.TASK_ID, "newTaskId");
        newContext.put(OdcConstants.ORGANIZATION_ID, "newOrganizationId");

        OnlineSchemaChangeContextHolder.retrace(newContext);

        assertEquals("newTaskWorkSpace", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("newTaskId", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("newOrganizationId", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        String taskWorkspace = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals(userId.toString(), taskWorkspace);

        String taskIdKey = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskId.toString(), taskIdKey);

        String organizationIdKey = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(organizationId.toString(), organizationIdKey);
    }
}
