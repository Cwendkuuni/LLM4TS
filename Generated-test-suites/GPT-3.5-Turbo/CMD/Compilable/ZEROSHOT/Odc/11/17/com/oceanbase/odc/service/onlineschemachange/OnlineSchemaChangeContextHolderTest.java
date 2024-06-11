package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> testContext;

    @Before
    public void setUp() {
        testContext = new HashMap<>();
        testContext.put("taskId", "123");
        testContext.put("onlineSchemaChangeWorkSpace", "testWorkspace");
        testContext.put("organizationId", "456");

        OnlineSchemaChangeContextHolder.retrace(testContext);
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(789L, 10L, 111L);

        assertEquals("789", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("10", MDC.get("taskId"));
        assertEquals("111", MDC.get("organizationId"));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("789", "10", "111");

        assertEquals("789", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("10", MDC.get("taskId"));
        assertEquals("111", MDC.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = MDC.getCopyOfContextMap();

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("123", MDC.get("taskId"));
        assertEquals("testWorkspace", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("456", MDC.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get("taskId"));
        assertNull(MDC.get("onlineSchemaChangeWorkSpace"));
        assertNull(MDC.get("organizationId"));
    }

    @Test
    public void testGet() {
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("testWorkspace", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));
        assertNull(OnlineSchemaChangeContextHolder.get("nonExistentKey"));
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }
}