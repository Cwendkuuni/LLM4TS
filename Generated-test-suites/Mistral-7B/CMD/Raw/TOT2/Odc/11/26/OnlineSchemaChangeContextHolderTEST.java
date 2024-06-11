import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithValidArguments() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals("1", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("2", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }
}