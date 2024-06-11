import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder holder;
    private Map<String, String> context;

    @Before
    public void setup() {
        holder = new OnlineSchemaChangeContextHolder();
        context = new HashMap<>();
    }

    @Test
    public void testTraceLong() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        holder.trace(userId, taskId, organizationId);
        assertEquals(userId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        String userId = "testUser";
        String taskId = "testTaskId";
        String organizationId = "testOrganizationId";
        holder.trace(userId, taskId, organizationId);
        Long userIdL = Long.parseLong(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Long taskIdL = Long.parseLong(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Long organizationIdL = Long.parseLong(MDC.get(OdcConstants.ORGANIZATION_ID));
        assertEquals(Long.parseLong(userId), userIdL);
        assertEquals(Long.parseLong(taskId), taskIdL);
        assertEquals(Long.parseLong(organizationId), organizationIdL);
    }

    // Add test cases for other methods
}