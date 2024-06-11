import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTrace_Long() {
        Long userId = 123L;
        Long taskId = 456L;
        Long orgId = 789L;
        
        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);
        
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTrace_String() {
        String userId = "123";
        String taskId = "456";
        String orgId = "789";
        
        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);
        
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "456");
        context.put(OdcConstants.ORGANIZATION_ID, "789");
        
        OnlineSchemaChangeContextHolder.retrace(context);
        
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "456");
        MDC.put(OdcConstants.ORGANIZATION_ID, "789");
        
        OnlineSchemaChangeContextHolder.clear();
        
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");
        
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
}