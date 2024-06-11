import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLongMethod() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringMethod() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        OnlineSchemaChangeContextHolder.trace("NaN", "2", "3"); // shall trow NumberFormatException
    }
  
    @Test
    public void testRetraceMethod() {
        Map<String, String> context = new HashMap<>();
        context.put("key", "value");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("value", OnlineSchemaChangeContextHolder.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testRetraceWithNull() {
        OnlineSchemaChangeContextHolder.retrace(null); // shall throw NullPointerException
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
}