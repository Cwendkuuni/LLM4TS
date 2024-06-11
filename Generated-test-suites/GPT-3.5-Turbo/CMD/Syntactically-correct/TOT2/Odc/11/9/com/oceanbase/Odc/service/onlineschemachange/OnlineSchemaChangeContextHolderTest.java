package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
        // initialize any required resources before each test case
    }

    @Test
    public void testTrace_WithLongValues() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get("organizationId"));
    }

    @Test
    public void testTrace_WithStringValues() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        MDC.put("testKey", "testValue");
        MDC.put("anotherTestKey", "anotherTestValue");
        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());
        assertEquals("testValue", MDC.get("testKey"));
        assertEquals("anotherTestValue", MDC.get("anotherTestKey"));
    }

    @Test
    public void testClear() {
        MDC.put("testKey", "testValue");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("testKey"));
    }

    @Test
    public void testGet() {
        MDC.put("testKey", "testValue");
        assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }
}