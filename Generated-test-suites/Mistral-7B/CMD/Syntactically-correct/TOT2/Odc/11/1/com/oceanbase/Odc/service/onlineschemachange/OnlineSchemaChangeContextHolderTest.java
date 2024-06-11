import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongArguments() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals("1", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("2", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", context.get(OnlineSchemaChangeConsts.ORGANIZATION_ID));
    }
}