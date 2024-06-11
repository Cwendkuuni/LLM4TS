import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLong() {
        // Test trace method with Long parameters
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        // Test trace method with String parameters
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        // Test retrace method with a context map
        MDC.put("testKey", "testValue");
        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());
        assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }

    @Test
    public void testClear() {
        // Test clear method to clear the context
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testGet() {
        // Test get method to retrieve a value from the context
        MDC.put("testKey", "testValue");
        assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }

}