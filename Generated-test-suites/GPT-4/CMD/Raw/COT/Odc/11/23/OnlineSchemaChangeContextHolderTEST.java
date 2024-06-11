
import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongParameters() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testTraceWithStringParameters() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testRetrace() {
        Map<String, String> retraceMap = new HashMap<>();
        retraceMap.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
        OnlineSchemaChangeContextHolder.retrace(retraceMap);
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }
    
    @Test
    public void testTraceWithNullParameters() {
        OnlineSchemaChangeContextHolder.trace((Long) null, (Long) null, (Long) null);
    }

    @Test
    public void testTraceWithEmptyStringParameters() {
        OnlineSchemaChangeContextHolder.trace("", "", "");
    }

    @Test
    public void testGetNonExistentKey() {
        assertNull(OnlineSchemaChangeContextHolder.get("nonExistentKey"));
    }
}
