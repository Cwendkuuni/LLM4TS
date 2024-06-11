import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", MDC.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "5");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "4");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("4", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("5", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "10");
        assertEquals("10", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

}