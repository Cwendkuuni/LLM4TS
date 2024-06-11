import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        // Test trace method with valid input
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("3", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTraceString() {
        // Test trace method with String input
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("3", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        // Test retrace method with valid input
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());
        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("3", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        // Test clear method
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }
}