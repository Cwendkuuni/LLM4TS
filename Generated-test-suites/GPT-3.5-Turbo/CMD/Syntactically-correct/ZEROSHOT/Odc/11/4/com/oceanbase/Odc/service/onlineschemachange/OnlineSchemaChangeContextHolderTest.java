import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> testContext;

    @Before
    public void setUp() {
        testContext = new HashMap<>();
        testContext.put("taskId", "123");
        testContext.put("onlineSchemaChangeWorkSpace", "testWorkspace");
        testContext.put("organizationId", "456");
    }

    @After
    public void tearDown() {
        testContext.clear();
    }

    @Test
    public void testTrace_Long_Long_Long() {
        Long userId = 789L;
        Long taskId = 123L;
        Long organizationId = 456L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assertEquals("789", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTrace_String_String_String() {
        String userId = "789";
        String taskId = "123";
        String organizationId = "456";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assertEquals("789", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(testContext);
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("testWorkspace", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }
}