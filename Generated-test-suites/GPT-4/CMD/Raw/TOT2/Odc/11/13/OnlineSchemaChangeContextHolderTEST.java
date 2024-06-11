import org.junit.Test;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    private final Long userId = 12345L;
    private final Long taskId = 67890L;
    private final Long organizationId = 111213L;
    private final String taskIdAsString = "67890";
    private final String userIdAsString = "12345";
    private final String organizationIdAsString = "111213";

    @Before
    public void setup() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test
    public void testTraceWithLong() {
        assertEquals(userIdAsString, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdAsString, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTraceWithString() {
        OnlineSchemaChangeContextHolder.trace(userIdAsString, taskIdAsString, organizationIdAsString);
        assertEquals(userIdAsString, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdAsString, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithStringNumberFormat() {
        OnlineSchemaChangeContextHolder.trace("badId", "badTask", "badOrganization");
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "newSpace");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("newSpace", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }
}