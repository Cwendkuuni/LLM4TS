package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", MDC.get("taskId"));
        assertEquals("3", MDC.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        // Assuming context map is initialized with values
        // Map<String, String> context = ...
        // OnlineSchemaChangeContextHolder.retrace(context);
        // Add test cases for checking if context values are set correctly in MDC
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("onlineSchemaChangeWorkSpace"));
        assertNull(MDC.get("taskId"));
        assertNull(MDC.get("organizationId"));
    }

    @Test
    public void testGet() {
        // Assuming keys are set in MDC
        // MDC.put("key1", "value1");
        // OnlineSchemaChangeContextHolder.get("key1");
        // Add assertion for testing if key1 is retrieved correctly
    }

}