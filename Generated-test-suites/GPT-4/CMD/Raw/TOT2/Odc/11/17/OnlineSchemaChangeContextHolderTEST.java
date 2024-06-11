import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;

public class OnlineSchemaChangeContextHolderTest {

    private static final String USER_ID = "userId";
    private static final String TASK_ID = "taskId";
    private static final String ORGANIZATION_ID = "organizationId";

    @Before
    public void setup() {
        MDC.clear();
    }

    @Test
    public void traceTest() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        Assert.assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals("1", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void traceWithStringParamsTest() {
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);
        Assert.assertEquals(USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals(ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void retraceTest() {
        HashMap<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "test");
        OnlineSchemaChangeContextHolder.retrace(context);
        Assert.assertEquals("test", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void clearTest() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        OnlineSchemaChangeContextHolder.clear();
        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void getTest() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "test");
        String val = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        Assert.assertEquals("test", val);
    }

    @Test(expected = NumberFormatException.class)
    public void stringParamsInvalidFormatTest() {
        OnlineSchemaChangeContextHolder.trace("invalid", "invalid", "invalid");
    }
}