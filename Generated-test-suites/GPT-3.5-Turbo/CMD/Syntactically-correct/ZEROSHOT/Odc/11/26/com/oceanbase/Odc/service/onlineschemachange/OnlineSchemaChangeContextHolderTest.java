package com.oceanbase.odc.service.onlineschemachange;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> contextMap;

    @Before
    public void setUp() {
        contextMap = new HashMap<>();
        contextMap.put("taskId", "123");
        contextMap.put("onlineSchemaChangeWorkSpace", "workspace");
    }

    @After
    public void tearDown() {
        contextMap = null;
    }

    @Test
    public void testTrace_Long_Long_Long() {
        OnlineSchemaChangeContextHolder.trace(1L, 123L, 456L);

        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTrace_String_String_String() {
        OnlineSchemaChangeContextHolder.trace("1", "123", null);

        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(contextMap);

        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("workspace", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 123L, 456L);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 123L, 456L);

        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));

        OnlineSchemaChangeContextHolder.clear();
    }
}