import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("1", "1", "1");
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTraceNullUserId() {
        OnlineSchemaChangeContextHolder.trace(null, 1L, 1L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceNullTaskId() {
        OnlineSchemaChangeContextHolder.trace(1L, null, 1L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceNullOrganizationId() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, null);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceEmptyUserId() {
        OnlineSchemaChangeContextHolder.trace("", 1L, 1L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceEmptyTaskId() {
        OnlineSchemaChangeContextHolder.trace(1L, "", 1L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceEmptyOrganizationId() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, "");
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceInvalidLongUserId() {
        OnlineSchemaChangeContextHolder.trace("abc", 1L, 1L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceInvalidLongTaskId() {
        OnlineSchemaChangeContextHolder.trace(1L, "abc", 1L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLargeStringUserId() {
        String userId = "1".repeat(1000);
        OnlineSchemaChangeContextHolder.trace(userId, 1L, 1L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLargeStringTaskId() {
        String taskId = "1".repeat(1000);
        OnlineSchemaChangeContextHolder.trace(1L, taskId, 1L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(taskId, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        Map<String, String> currentContext = MDC.getCopyOfContextMap();
        assertEquals(context, currentContext);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        String value = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals("1", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testConcurrentAccess() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
    }

    @Test(expected = SecurityException.class)
    public void testUnauthorizedAccess() {
        long userId = 1L;
        long organizationId = 1L;
        SecurityContextUtils.setCurrentUser(2L, organizationId, null);
        OnlineSchemaChangeContextHolder.trace(userId, "test", organizationId);
    }
}