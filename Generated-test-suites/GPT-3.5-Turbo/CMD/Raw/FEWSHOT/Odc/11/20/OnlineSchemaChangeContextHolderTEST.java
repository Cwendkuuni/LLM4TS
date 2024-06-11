package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> contextMap;

    @Before
    public void setUp() {
        contextMap = new HashMap<>();
        contextMap.put("taskId", "123");
        contextMap.put("onlineSchemaChangeWorkSpace", "workspace");
        contextMap.put("organizationId", "456");
    }

    @After
    public void tearDown() {
        contextMap = null;
    }

    @Test
    public void testTraceLongLongLong() {
        OnlineSchemaChangeContextHolder.trace(111L, 222L, 333L);
        assertEquals("111", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("222", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("333", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTraceStringStringString() {
        OnlineSchemaChangeContextHolder.trace("111", "222", "333");
        assertEquals("111", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("222", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("333", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(contextMap);
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("workspace", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

}