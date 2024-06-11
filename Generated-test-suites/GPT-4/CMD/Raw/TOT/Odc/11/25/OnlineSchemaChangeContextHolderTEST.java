import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongParameters() {
        Long userid = 1L;
        Long taskId = 100L;
        Long organizationId = 500L;
        OnlineSchemaChangeContextHolder.trace(userid, taskId, organizationId);
        Assert.assertEquals(userid.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userid = "200";
        String taskId = "300";
        String organizationId = "400";
        OnlineSchemaChangeContextHolder.trace(userid, taskId, organizationId);
        Assert.assertEquals(userid, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        String userid = "200";
        String taskId = "300";
        String organizationId = "400";
        OnlineSchemaChangeContextHolder.trace(userid, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testretrace() {
        Map<String,String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "100");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "200");
        context.put(OdcConstants.ORGANIZATION_ID, "300");
        OnlineSchemaChangeContextHolder.retrace(context);
        Assert.assertEquals("100", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("200", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals("300", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}