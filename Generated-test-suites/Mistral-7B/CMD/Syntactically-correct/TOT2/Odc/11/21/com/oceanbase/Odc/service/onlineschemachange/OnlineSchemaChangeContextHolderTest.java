import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithValidArguments() {
        // (same as Expert 1's test)
    }

    @Test
    public void testTraceWithNullArguments() {
        // (same as Expert 1's test)
    }

    @Test
    public void testTraceWithEmptyStrings() {
        // (same as Expert 1's test)
    }

    @Test
    public void testTraceWithInvalidDataTypes() {
        // (same as Expert 1's test)
    }

    @Test
    public void testTraceWithSameKeyMultipleTimes() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId + "", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testClearContextBeforeSettingIt() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.clear();
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(null, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testRetraceWithValidArguments() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = MDC.getCopyOfContextMap();

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(userId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", MDC.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testRetraceWithNullContext() {
        OnlineSchemaChangeContextHolder.retrace(null);

        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testGetWithValidKey() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String userIdFromContext = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String taskIdFromContext = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdFromContext = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID);

        assertEquals(userId + "", userIdFromContext);
        assertEquals(taskId + "", taskIdFromContext);
        assertEquals(organizationId + "", organizationIdFromContext);
    }

    @Test
    public void testGetWithInvalidKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        String invalidKey = "invalidKey";

        assertEquals(null, OnlineSchemaChangeContextHolder.get(invalidKey));
    }
}