package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> testContextMap;

    @Before
    public void setUp() {
        testContextMap = new HashMap<>();
        testContextMap.put("taskId", "123");
        testContextMap.put("onlineSchemaChangeWorkSpace", "456");
        testContextMap.put("organizationId", "789");
        OnlineSchemaChangeContextHolder.retrace(testContextMap);
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithLong() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);

        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTraceWithString() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");

        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> contextMap = new HashMap<>();
        contextMap.put("taskId", "987");
        contextMap.put("onlineSchemaChangeWorkSpace", "654");
        contextMap.put("organizationId", "321");

        OnlineSchemaChangeContextHolder.retrace(contextMap);

        assertEquals("987", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("654", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("321", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }
}