import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLongs() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTraceStrings() {
        OnlineSchemaChangeContextHolder.trace("1", "1", "1");
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("testKey", "testValue");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("1", "1", "1");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testGet() {
        assertNull(OnlineSchemaChangeContextHolder.get("nonExistentKey"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTraceNullStrings() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTraceNullLongs() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetraceEmptyMap() {
        OnlineSchemaChangeContextHolder.retrace(new HashMap<>());
    }
}