import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongUserId() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringUserId() {
        Long userId = 1L;
        String userIdStr = userId.toString();
        Long taskId = 2L;
        String taskIdStr = taskId.toString();
        String organizationIdStr = "1";
        Long organizationId = Long.parseLong(organizationIdStr);

        OnlineSchemaChangeContextHolder.trace(userIdStr, taskIdStr, organizationIdStr);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userIdStr, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdStr, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> contextAfterRetrace = MDC.getCopyOfContextMap();
        assertEquals(context, contextAfterRetrace);
    }

    @Test
    public void testClear() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> contextAfterClear = MDC.getCopyOfContextMap();
        assertNull(contextAfterClear);
    }

    @Test
    public void testGet() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");

        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals("123", taskId);
    }

    @Test
    public void testHandleNullInputs() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context);

        OnlineSchemaChangeContextHolder.trace(1L, null, null);
        context = MDC.getCopyOfContextMap();
        assertEquals(Long.toString(1), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.trace(null, 2L, null);
        context = MDC.getCopyOfContextMap();
        assertNull(context);

        OnlineSchemaChangeContextHolder.trace(null, null, 3L);
        context = MDC.getCopyOfContextMap();
        assertNull(context);
    }

    @Test
    public void testGetWithNonExistentKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        String nonExistentKey = "nonExistentKey";
        String value = OnlineSchemaChangeContextHolder.get(nonExistentKey);
        assertNull(value);
    }

    @Test
    public void testClearWithEmptyContextMap() {
        OnlineSchemaChangeContextHolder.clear();

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context);
    }

    @Test
    public void testGetWithEmptyContextMap() {
        OnlineSchemaChangeContextHolder.clear();

        String key = OnlineSchemaChangeContextHolder.TASK_ID;
        String value = OnlineSchemaChangeContextHolder.get(key);
        assertNull(value);
    }

    @Test
    public void testUpdateContextMapWithSameKeys() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        Map<String, String> contextAfterUpdate = MDC.getCopyOfContextMap();
        assertEquals("123", contextAfterUpdate.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
}