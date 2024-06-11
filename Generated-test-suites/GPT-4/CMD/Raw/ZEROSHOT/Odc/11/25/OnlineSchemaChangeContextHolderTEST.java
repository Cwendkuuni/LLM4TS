
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    @BeforeClass
    public static void setUpClass() {
        // Clear MDC before start
        MDC.clear();
    }

    @Before
    public void setUp() {
        // Clear MDC before each test
        MDC.clear();
    }

    @Test
    public void testTraceWithLongs() {
        Long userId = 123L;
        Long taskId = 456L;
        Long organizationId = 789L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStrings() {
        String userId = "123";
        String taskId = "456";
        String organizationId = "789";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("value1", MDC.get("key1"));
        assertEquals("value2", MDC.get("key2"));
    }
    
    @Test
    public void testClear() {
        MDC.put("test", "value");
        assertEquals("value", MDC.get("test"));

        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get("test"));
    }

    @Test
    public void testGet() {
        String key = "key";
        String value = "value";
        MDC.put(key, value);

        String storedValue = OnlineSchemaChangeContextHolder.get(key);

        assertEquals(value, storedValue);
    }
}
