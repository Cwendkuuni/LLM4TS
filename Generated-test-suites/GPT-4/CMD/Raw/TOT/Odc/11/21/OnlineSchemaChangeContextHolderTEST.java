import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    private static final String TEST_USER_ID = "12345";
    private static final String TEST_TASK_ID = "67890";
    private static final String TEST_ORGANIZATION_ID = "111213";

    private static final String TEST_KEY = "taskId";

    @Before
    public void setup() {
        OnlineSchemaChangeContextHolder.clear(); // Clearing the context before every test
    }

    @Test
    public void trace_withLongs() {
        OnlineSchemaChangeContextHolder.trace(Long.valueOf(TEST_USER_ID), Long.valueOf(TEST_TASK_ID), Long.valueOf(TEST_ORGANIZATION_ID));
        assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void trace_withStrings() {
        OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORGANIZATION_ID);
        assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void retrace_withContext() {
        Map<String, String> context = new HashMap<>();
        context.put(TEST_KEY, TEST_TASK_ID);
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(TEST_TASK_ID, MDC.get(TEST_KEY));
    }

    @Test
    public void clear_test() {
        MDC.put(TEST_KEY, TEST_TASK_ID);
        assertEquals(TEST_TASK_ID, MDC.get(TEST_KEY));
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(TEST_KEY));
    }

    @Test
    public void get_test() {
        MDC.put(TEST_KEY, TEST_TASK_ID);
        assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(TEST_KEY));
        assertNull(OnlineSchemaChangeContextHolder.get(null));
    }

    @Test(expected = NumberFormatException.class)
    public void trace_withInvalidStrings() {
        OnlineSchemaChangeContextHolder.trace("INVALID_USER_ID", "INVALID_TASK_ID", "INVALID_ORGANIZATION_ID");
    }

    @Test(expected = NumberFormatException.class)
    public void trace_withSomeInvalidStrings() {
        OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, "INVALID_TASK_ID", TEST_ORGANIZATION_ID);
    }

}