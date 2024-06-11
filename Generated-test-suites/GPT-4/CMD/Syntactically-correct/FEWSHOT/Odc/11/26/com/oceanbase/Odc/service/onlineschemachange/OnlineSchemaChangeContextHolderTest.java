
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        Long userId = 123L;
        Long taskId = 456L;
        Long orgId = 789L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(orgId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        String userId = "123";
        String taskId = "456";
        String orgId = "789";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(orgId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");
        context.put("key3", "value3");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("value1", MDC.get("key1"));
        assertEquals("value2", MDC.get("key2"));
        assertEquals("value3", MDC.get("key3"));
    }

    @Test
    public void testClear() {
        MDC.put("key1", "value");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("key1"));
    }

    @Test
    public void testGet() {
        String value = "value";
        String key = "key";
        MDC.put(key, value);

        assertEquals(value, MDC.get(key));
        assertEquals(value, OnlineSchemaChangeContextHolder.get(key));
    }
}
