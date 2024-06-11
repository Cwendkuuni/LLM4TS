import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        // Test trace method with Long parameters
        Long userId = 1L;
        Long taskId = 123L;
        Long organizationId = 456L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        // Test retrace method with context map
        OnlineSchemaChangeContextHolder.clear(); // Clear any existing values

        Map<String, String> contextMap = new HashMap<>();
        contextMap.put("onlineSchemaChangeWorkSpace", "2");
        contextMap.put("taskId", "345");
        contextMap.put("organizationId", "789");

        OnlineSchemaChangeContextHolder.retrace(contextMap);

        assertEquals("2", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("345", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        // Test clear method to ensure MDC context is cleared
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testGet() {
        // Test get method to retrieve values from MDC context
        OnlineSchemaChangeContextHolder.clear(); // Clear any existing values
        OnlineSchemaChangeContextHolder.trace(1L, 123L, 456L);

        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

}