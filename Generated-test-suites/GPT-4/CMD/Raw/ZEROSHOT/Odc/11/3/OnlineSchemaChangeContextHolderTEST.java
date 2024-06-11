
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {
    private final String fakeTaskId = "123";
    private final String fakeUserId = "456";
    private final String fakeOrganizationId = "789";

    @Before
    public void setup() {
        MDC.clear(); // Clean the context before each test
    }

    @Test
    public void testTraceWithLongParameters() {
        OnlineSchemaChangeContextHolder.trace(Long.parseLong(fakeUserId), Long.parseLong(fakeTaskId), Long.parseLong(fakeOrganizationId));

        // Assert that the values have been correctly set in the context
        assertEquals(fakeUserId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(fakeTaskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(fakeOrganizationId, OnlineSchemaChangeContextHolder.get(com.oceanbase.odc.core.shared.constant.OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        OnlineSchemaChangeContextHolder.trace(fakeUserId, fakeTaskId, fakeOrganizationId);

        // Assert that the values have been correctly set in the context
        assertEquals(fakeUserId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(fakeTaskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(fakeOrganizationId, OnlineSchemaChangeContextHolder.get(com.oceanbase.odc.core.shared.constant.OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, fakeTaskId);
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, fakeUserId);
        context.put(com.oceanbase.odc.core.shared.constant.OdcConstants.ORGANIZATION_ID, fakeOrganizationId);

        // Set the context
        OnlineSchemaChangeContextHolder.retrace(context);

        // Assert that the values have been correctly set in the context
        assertEquals(fakeUserId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(fakeTaskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(fakeOrganizationId, OnlineSchemaChangeContextHolder.get(com.oceanbase.odc.core.shared.constant.OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(fakeUserId, fakeTaskId, fakeOrganizationId);
        OnlineSchemaChangeContextHolder.clear();

        // Assert that the context has been cleared
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(com.oceanbase.odc.core.shared.constant.OdcConstants.ORGANIZATION_ID));
    }
}
