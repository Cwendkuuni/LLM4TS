import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        // Typical use case
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("1", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("1", OnlineSchemaChangeContextHolder.get("organizationId"));
        
        // Edge case
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));

        // Error scenario
        OnlineSchemaChangeContextHolder.trace("test", "test", "test");
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        // Typical use case
        Map<String, String> context = new HashMap<>();
        context.put("onlineSchemaChangeWorkSpace", "2");
        context.put("taskId", "2");
        context.put("organizationId", "2");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("2", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("2", OnlineSchemaChangeContextHolder.get("organizationId"));

        // Edge case
        Map<String, String> emptyContext = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(emptyContext);
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        // Clear the context
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

}