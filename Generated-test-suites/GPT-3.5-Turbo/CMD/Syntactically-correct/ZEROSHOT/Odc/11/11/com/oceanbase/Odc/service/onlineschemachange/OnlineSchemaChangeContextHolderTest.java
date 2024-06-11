import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> testContext;

    @Before
    public void setUp() {
        testContext = new HashMap<>();
        testContext.put("taskId", "12345");
        testContext.put("onlineSchemaChangeWorkSpace", "testWorkspace");
        testContext.put("organizationId", "6789");
    }

    @Test
    public void testTrace_Long() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTrace_String() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(testContext);
        assertEquals("12345", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("testWorkspace", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("6789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.retrace(testContext);
        assertEquals("12345", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("testWorkspace", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("6789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }
}