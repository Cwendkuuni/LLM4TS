package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("3", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(Map.of("onlineSchemaChangeWorkSpace", "4", "taskId", "5", "organizationId", "6"));
        assertEquals("4", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("5", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("6", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }
  
    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(7L, 8L, 9L);
        assertEquals("7", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("8", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("9", OnlineSchemaChangeContextHolder.get("organizationId"));
    }
}