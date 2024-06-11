package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("onlineSchemaChangeWorkSpace", "111");
        context.put("taskId", "222");
        context.put("organizationId", "333");

        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("111", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("222", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("333", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }
}