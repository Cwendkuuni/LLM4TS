package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> testContext;

    @Before
    public void setUp() {
        testContext = new HashMap<>();
        testContext.put("userId", "123");
        testContext.put("taskId", "456");
        testContext.put("organizationId", "789");
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("456", MDC.get("taskId"));
        assertEquals("789", MDC.get("organizationId"));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("456", MDC.get("taskId"));
        assertEquals("789", MDC.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(testContext);
        assertEquals("123", MDC.get("userId"));
        assertEquals("456", MDC.get("taskId"));
        assertEquals("789", MDC.get("organizationId"));
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