import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithValidArguments() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        Map<String, String> context = MDC.getContextMap();
        assertEquals(1L, Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_ID)));
        assertEquals(2L, Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)));
        assertEquals(3L, Long.parseLong(context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID)));
    }

    @Test
    public void testTraceWithStringArguments() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        Map<String, String> context = MDC.getContextMap();
        Long userId = Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Long taskId = Long.parseLong(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Long organizationId = Long.parseLong(context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
        assertEquals("1", String.valueOf(userId));
        assertEquals("2", String.valueOf(taskId));
        assertEquals("3", String.valueOf(organizationId));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(context, MDC.getContextMap());
    }

    @Test
    public void testClear() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testGetMethod() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTraceWithNullArguments() {
        OnlineSchemaChangeContextHolder.trace(null, 2L, 3L);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithNegativeArguments() {
        OnlineSchemaChangeContextHolder.trace(-1L, 2L, 3L);
    }

    @Test
    public void testTraceWithEmptyMap() {
        OnlineSchemaChangeContextHolder.retrace(new HashMap<>());
        OnlineSchemaChangeContextHolder.trace(1L, 2L, null);
        assertEquals(0, MDC.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetMethodWithNullKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(new HashMap<>());
        assertNull(OnlineSchemaChangeContextHolder.get(null));
    }

    @Test(expected = NullPointerException.class)
    public void testGetMethodWithEmptyContext() {
        OnlineSchemaChangeContextHolder.get("testKey");
    }
}
//