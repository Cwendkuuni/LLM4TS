package com.oceanbase.odc.service.onlineschemachange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeContextHolderTest {

    @Mock
    MDC mdcMock;

    @InjectMocks
    OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLong() {
        Long userId = 123L;
        Long taskId = 456L;
        Long organizationId = 789L;

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithString() {
        String userId = "123";
        String taskId = "456";
        String organizationId = "789";

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        String userId = "123";
        String taskId = "456";
        String organizationId = "789";

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        onlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        String userId = "123";
        onlineSchemaChangeContextHolder.trace(userId, null, null);
        String retrievedUserId = onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);

        assertEquals(userId, retrievedUserId);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");

        onlineSchemaChangeContextHolder.retrace(context);

        String retracedValue = MDC.get("key1");
        assertEquals("value1", retracedValue);
    }

}
