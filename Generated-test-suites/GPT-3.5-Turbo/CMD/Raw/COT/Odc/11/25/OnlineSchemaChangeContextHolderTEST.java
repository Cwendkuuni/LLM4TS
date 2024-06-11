import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        // Test trace method with Long parameters
        Long userId = 123L;
        Long taskId = 456L;
        Long organizationId = 789L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));

        // Test trace method with String parameters
        String userIdStr = "123";
        String taskIdStr = "456";
        String organizationIdStr = "789";
        OnlineSchemaChangeContextHolder.trace(userIdStr, taskIdStr, organizationIdStr);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        // Test retrace method by setting and getting context map
        MDC.put("key1", "value1");
        MDC.put("key2", "value2");
        MDC.put("key3", "value3");

        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());

        assertEquals("value1", MDC.get("key1"));
        assertEquals("value2", MDC.get("key2"));
        assertEquals("value3", MDC.get("key3"));
    }

    @Test
    public void testClear() {
        // Test clear method to clear the MDC context
        MDC.put("key1", "value1");
        MDC.put("key2", "value2");
        MDC.put("key3", "value3");

        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get("key1"));
        assertNull(MDC.get("key2"));
        assertNull(MDC.get("key3"));
    }

    @Test
    public void testGet() {
        // Test get method to retrieve MDC key values
        MDC.put("testKey", "testValue");

        assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }
}