
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;
import com.oceanbase.odc.service.onlineschemachange.*;

public class OnlineSchemaChangeContextHolderTest {

    private static final String TASK_ID = "taskId";
    private static final String TASK_WORK_SPACE = "traceId";

    @Before
    public void setUp(){
        MDC.clear();
    }

    @Test
    public void testTrace() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        assertEquals("1", MDC.get(TASK_WORK_SPACE));
        assertEquals("1", MDC.get(TASK_ID));
    }

    @Test
    public void testTraceWithStrings() {
        OnlineSchemaChangeContextHolder.trace("1", "1", "1");
        assertEquals("1", MDC.get(TASK_WORK_SPACE));
        assertEquals("1", MDC.get(TASK_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(TASK_WORK_SPACE, "2");
        context.put(TASK_ID, "2");

        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("2", MDC.get(TASK_WORK_SPACE));
        assertEquals("2", MDC.get(TASK_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(TASK_WORK_SPACE));
        assertNull(MDC.get(TASK_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get("non_exist_key"));
    }
}
