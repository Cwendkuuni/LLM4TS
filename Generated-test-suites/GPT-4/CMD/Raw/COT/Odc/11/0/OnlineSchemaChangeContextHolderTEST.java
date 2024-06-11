
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() throws Exception {
        MDC.clear();
    }

    @After
    public void tearDown() throws Exception {
        MDC.clear();
    }

    @Test
    public void traceLong() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void traceString() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void traceStringNull() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void retrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test(expected = NullPointerException.class)
    public void retraceException() {
        OnlineSchemaChangeContextHolder.retrace(null);
    }

    @Test
    public void clear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void get() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
        String value = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals("456", value);
    }
}
