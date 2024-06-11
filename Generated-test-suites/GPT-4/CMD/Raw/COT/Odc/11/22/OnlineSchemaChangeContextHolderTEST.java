import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

	@Before
    public void clearContext() {
        // Ensure context is cleared before each test
        OnlineSchemaChangeContextHolder.clear();
    }
	
	@Test
    public void testTraceLong() {
        Long userId = 1234L;
        Long taskId = 5678L;
        Long orgId = 91011L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // ...
    }

    // Testing trace method with strings
    @Test
    public void testTraceString() {
        String userId = "1234";
        String taskId = "5678";
        String orgId = "910";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // ...
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringNumberFormatException() {
        OnlineSchemaChangeContextHolder.trace("notANumber", "5678", "910");
    }

    // Testing retrace method
    @Test
    public void testRetrace() {
        Map<String,String> context = new HashMap<>();
        context.put("key1", "value1");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("value1", OnlineSchemaChangeContextHolder.get("key1"));
    }
	
    // Testing clear method
    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("1234", "5678", "910");

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // ...
    }
	
    // Testing get method
    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace("1234", "5678", "910");

        assertEquals("1234", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("5678", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // ...
    }

    @Test
    public void testGetNull() {
        assertNull(OnlineSchemaChangeContextHolder.get("nonExistentKey"));
    }
}