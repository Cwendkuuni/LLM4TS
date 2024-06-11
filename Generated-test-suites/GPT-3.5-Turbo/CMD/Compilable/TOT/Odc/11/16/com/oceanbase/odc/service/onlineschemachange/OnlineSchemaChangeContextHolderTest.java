package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace_Long() {
        OnlineSchemaChangeContextHolder.trace(1L, 100L, 200L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("100", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("200", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTrace_String() {
        OnlineSchemaChangeContextHolder.trace("1", "100", "200");
        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("100", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("200", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("onlineSchemaChangeWorkSpace", "2");
        context.put("taskId", "101");
        context.put("organizationId", "201");

        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("2", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("101", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("201", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

}