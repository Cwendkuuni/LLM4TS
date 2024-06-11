import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace_Long() {
        Long userId = 123L;
        Long taskId = 456L;
        Long orgId = 789L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get("orgId"));

        verify(SecurityContextUtils).setCurrentUser(userId, orgId, null);
    }

    @Test
    public void testTrace_String() {
        String userId = "123";
        String taskId = "456";
        String orgId = "789";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get("orgId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
        context.put("orgId", "789");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get("orgId"));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
        MDC.put("orgId", "789");

        OnlineSchemaChangeContextHolder.clear();

        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, MDC.get("orgId"));
    }

    @Test
    public void testGet() {
        MDC.put("testKey", "testValue");

        assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }
}