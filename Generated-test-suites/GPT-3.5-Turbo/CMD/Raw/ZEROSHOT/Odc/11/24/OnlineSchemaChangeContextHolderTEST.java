import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> testContext;

    @Before
    public void setUp() {
        testContext = new HashMap<>();
        testContext.put("taskId", "123");
        testContext.put("onlineSchemaChangeWorkSpace", "456");
        testContext.put("organizationId", "789");
    }

    @After
    public void tearDown() {
        testContext.clear();
    }

    @Test
    public void testTrace_Long_Long_Long() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTrace_String_String_String() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(testContext);
        assertEquals("123", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

}