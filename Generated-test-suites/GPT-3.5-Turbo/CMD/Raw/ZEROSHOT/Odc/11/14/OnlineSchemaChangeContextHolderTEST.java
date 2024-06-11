import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() throws Exception {
        // Setup logic before each test
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        Long userId = 123456L;
        Long taskId = 7890L;
        Long organizationId = 4567L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String workSpace = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String taskIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String orgId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);

        assertEquals(userId.toString(), workSpace);
        assertEquals(taskId.toString(), taskIdStr);
        assertEquals(organizationId.toString(), orgId);
    }

    @Test
    public void testTraceString() {
        String userId = "123456";
        String taskId = "7890";
        String organizationId = "4567";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String workSpace = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String taskIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String orgId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);

        assertEquals(userId, workSpace);
        assertEquals(taskId, taskIdStr);
        assertEquals(organizationId, orgId);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123456");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "7890");
        context.put(OdcConstants.ORGANIZATION_ID, "4567");

        OnlineSchemaChangeContextHolder.retrace(context);

        String workSpace = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String taskIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String orgId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);

        assertEquals("123456", workSpace);
        assertEquals("7890", taskIdStr);
        assertEquals("4567", orgId);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(123456L, 7890L, 4567L);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(123456L, 7890L, 4567L);

        String workSpace = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String taskIdStr = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String orgId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);

        assertEquals("123456", workSpace);
        assertEquals("7890", taskIdStr);
        assertEquals("4567", orgId);
    }
}