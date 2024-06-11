import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTrace_Long() {
        Long userId = 1L;
        Long taskId = 100L;
        Long organizationId = 500L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("100", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("500", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTrace_String() {
        String userId = "1";
        String taskId = "100";
        String organizationId = "500";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("1", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("100", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("500", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTrace_Null() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("onlineSchemaChangeWorkSpace", "2");
        context.put("taskId", "200");
        context.put("organizationId", "600");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("2", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("200", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("600", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 100L, 500L);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

}