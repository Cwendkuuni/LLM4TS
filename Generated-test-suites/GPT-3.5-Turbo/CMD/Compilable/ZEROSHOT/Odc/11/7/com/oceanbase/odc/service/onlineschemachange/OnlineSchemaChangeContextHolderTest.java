package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> testContext;

    @Before
    public void setUp() {
        testContext = new HashMap<>();
        testContext.put("taskId", "123");
        testContext.put("onlineSchemaChangeWorkSpace", "456");
        testContext.put("organizationId", "789");
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("3", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("4", "5", "6");
        assertEquals("4", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("5", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("6", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(testContext);
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.retrace(testContext);
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
    }

}