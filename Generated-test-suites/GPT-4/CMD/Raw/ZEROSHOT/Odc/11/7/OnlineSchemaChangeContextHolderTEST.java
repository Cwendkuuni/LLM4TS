
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.slf4j.MDC;
import java.util.HashMap;

public class OnlineSchemaChangeContextHolderTest {
  
    @Test
    public void testTraceLongParameters() {
        String userId = "123";
        String taskId = "234";
        String orgId = "345";

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.trace(Long.parseLong(userId), Long.parseLong(taskId), Long.parseLong(orgId));

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(orgId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringParameters() {
        String userId = "123";
        String taskId = "234";
        String orgId = "345";

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(orgId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        HashMap<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");

        MDC.clear();
        assertTrue(MDC.getCopyOfContextMap().isEmpty());

        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(context, MDC.getCopyOfContextMap());
    }

    @Test
    public void testClear() {
        MDC.put("key", "value");
        assertEquals("value", MDC.get("key"));

        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("key"));
    }

    @Test
    public void testGet() {
        String key = "key";
        String value = "value";

        MDC.put(key, value);
        assertEquals(value, OnlineSchemaChangeContextHolder.get(key));
    }
}
