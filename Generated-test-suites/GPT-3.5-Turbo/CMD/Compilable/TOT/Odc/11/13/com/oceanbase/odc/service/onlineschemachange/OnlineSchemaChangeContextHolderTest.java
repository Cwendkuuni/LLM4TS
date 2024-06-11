package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;


public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace_Long_Long_Long() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("3", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTrace_String_String_String() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("3", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("onlineSchemaChangeWorkSpace", "4");
        context.put("taskId", "5");
        context.put("organizationId", "6");

        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("4", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("5", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("6", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

}