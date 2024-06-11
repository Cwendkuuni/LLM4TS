import static org.junit.Assert.*;
import org.junit.*;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {
    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("testKey", "testValue");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("testValue", MDC.get("testKey"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("anything"));
    }

    @Test
    public void testGet() {
        MDC.put("getKey", "getValue");
        assertEquals("getValue", OnlineSchemaChangeContextHolder.get("getKey"));
    }

    @Test
    public void testWithNullValues() {
        OnlineSchemaChangeContextHolder.trace((Long) null, null, null);
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));

        OnlineSchemaChangeContextHolder.trace((String) null, null, null);
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test(expected = NumberFormatException.class)
    public void testWithInvalidLongValue() {
        OnlineSchemaChangeContextHolder.trace("notALongValue", "2", "3");
    }
}