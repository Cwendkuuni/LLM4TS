import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() throws Exception {
        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void tearDown() throws Exception {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLongLongLong() {
        Long userId = 123L;
        Long taskId = 456L;
        Long organizationId = 789L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTraceStringStringString() {
        String userId = "123";
        String taskId = "456";
        String organizationId = "789";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
        context.put("organizationId", "789");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

}