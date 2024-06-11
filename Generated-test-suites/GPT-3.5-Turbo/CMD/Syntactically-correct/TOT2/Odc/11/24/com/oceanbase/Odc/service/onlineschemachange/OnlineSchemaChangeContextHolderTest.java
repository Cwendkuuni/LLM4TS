import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace_LongLongLong() {
        Long userId = 123L;
        Long taskId = 456L;
        Long organizationId = 789L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTrace_StringStringString() {
        String userId = "123";
        String taskId = "456";
        String organizationId = "789";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("onlineSchemaChangeWorkSpace", "123");
        context.put("taskId", "456");
        context.put("organizationId", "789");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("123", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertNull(OnlineSchemaChangeContextHolder.get("taskId"));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }
}