
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() throws Exception {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        Long userId = 123L;
        Long taskId = 456L;
        Long organizationId = 789L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        String userId = "321";
        String taskId = "654";
        String organizationId = "987";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("321", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("654", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("987", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");
        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("value1", OnlineSchemaChangeContextHolder.get("key1"));
        assertEquals("value2", OnlineSchemaChangeContextHolder.get("key2"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("321", "654", "987");
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @After
    public void tearDown() throws Exception {
        OnlineSchemaChangeContextHolder.clear();
    }
}
