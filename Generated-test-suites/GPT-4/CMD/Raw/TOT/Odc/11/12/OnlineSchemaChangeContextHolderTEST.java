import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTrace_withLongParameters() {
        long userId = 1L;
        long taskId = 1L;
        long organizationId = 1L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTrace_withNullStringParameters() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "test_space");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "test_id");

        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("test_space", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("test_id", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "test_space");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "test_id");

        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "test_space");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "test_id");

        assertEquals("test_space", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("test_id", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
}