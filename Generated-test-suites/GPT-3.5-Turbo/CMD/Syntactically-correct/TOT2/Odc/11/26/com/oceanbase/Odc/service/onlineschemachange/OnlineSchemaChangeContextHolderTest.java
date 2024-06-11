import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTrace_long() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        Assert.assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("3", MDC.get("organizationId"));
    }

    @Test
    public void testTrace_string() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        Assert.assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("3", MDC.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put("organizationId", "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        Assert.assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("3", MDC.get("organizationId"));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        MDC.put("organizationId", "3");

        OnlineSchemaChangeContextHolder.clear();

        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNull(MDC.get("organizationId"));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");

        String value = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);

        Assert.assertEquals("2", value);
    }
}