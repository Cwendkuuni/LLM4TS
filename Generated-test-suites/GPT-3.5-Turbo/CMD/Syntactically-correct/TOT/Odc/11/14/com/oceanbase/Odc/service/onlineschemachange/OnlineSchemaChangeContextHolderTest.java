import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceLong() {
        Long userId = 123L;
        Long taskId = 456L;
        Long organizationId = 789L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(String.valueOf(userId), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(String.valueOf(taskId), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(String.valueOf(organizationId), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        String userId = "123";
        String taskId = "456";
        String organizationId = "789";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("taskId", "123");
        context.put("onlineSchemaChangeWorkSpace", "456");
        context.put("organizationId", "789");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        MDC.put("taskId", "123");
        MDC.put("onlineSchemaChangeWorkSpace", "456");
        MDC.put("organizationId", "789");

        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put("key1", "value1");
        
        String value = OnlineSchemaChangeContextHolder.get("key1");

        assertEquals("value1", value);
    }

}