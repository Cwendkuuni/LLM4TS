
import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = OnlineSchemaChangeContextHolder.getContextMap();
        assertEquals(userId + "", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NullPointerException.class)
    public void testTraceNullUserId() {
        OnlineSchemaChangeContextHolder.trace(null, 2L, 3L);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceNonLongUserId() {
        OnlineSchemaChangeContextHolder.trace("1", "2", 3L);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceNonLongTaskId() {
        OnlineSchemaChangeContextHolder.trace(1L, "2", 3L);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceNonLongOrganizationId() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, "3");
    }

    @Test
    public void testTraceEmptyContext() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(0, context.size());

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        context = OnlineSchemaChangeContextHolder.getContextMap();
        assertEquals(3, context.size());

        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(0, context.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetContextMapBeforeTrace() {
        OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        Map<String, String> context = OnlineSchemaChangeContextHolder.getContextMap();
        assertEquals(3, context.size());
        OnlineSchemaChangeContextHolder.clear();
        context = OnlineSchemaChangeContextHolder.getContextMap();
        assertEquals(0, context.size());
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        Map<String, String> context = OnlineSchemaChangeContextHolder.getContextMap();
        assertEquals(OdcConstants.ORGANIZATION_ID, context.keySet().toString());
        assertEquals(3L + "", context.get(OdcConstants.ORGANIZATION_ID));
    }
}
