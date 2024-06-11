import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithLongParameters() {
        Long userId = 1L;
        Long taskId = 123L;
        Long organizationId = 456L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "123";
        String organizationId = "456";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> contextMap = new HashMap<>();
        contextMap.put("onlineSchemaChangeWorkSpace", "2");
        contextMap.put("taskId", "456");
        contextMap.put("organizationId", "789");

        OnlineSchemaChangeContextHolder.retrace(contextMap);

        assertEquals("2", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 123L, 456L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }
}