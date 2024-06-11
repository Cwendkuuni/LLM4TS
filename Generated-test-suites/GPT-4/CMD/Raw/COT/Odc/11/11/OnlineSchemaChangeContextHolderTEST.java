import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear(); // Clean up after each test
    }

    @Test
    public void testTraceWithLongParams() {
        OnlineSchemaChangeContextHolder.trace(12345L, 67890L, 99999L);
        assertEquals("12345", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("67890", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("99999", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParams() {
        OnlineSchemaChangeContextHolder.trace("12345", "67890", "99999");
        assertEquals("12345", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("67890", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("99999", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidStringParams() {
        OnlineSchemaChangeContextHolder.trace("abc", "def", "ghi");
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("value1", MDC.get("key1"));
        assertEquals("value2", MDC.get("key2"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(12345L, 67890L, 99999L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put("key", "value");
        assertEquals("value", OnlineSchemaChangeContextHolder.get("key"));
    }
}