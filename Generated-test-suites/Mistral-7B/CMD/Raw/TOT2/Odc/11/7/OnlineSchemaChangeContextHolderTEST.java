import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.HashMap;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLong(){
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.OdcConstants.ORGANIZATION_ID);
        assertEquals("1", taskId);
        assertEquals("1", organizationId);
    }

    @Test
    public void testTraceString(){
        OnlineSchemaChangeContextHolder.trace("1", "1", "1");
        String userId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.OdcConstants.ORGANIZATION_ID);
        assertEquals("1", userId);
        assertEquals("1", taskId);
        assertEquals("1", organizationId);
    }

    @Test
    public void testRetrace(){
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.OdcConstants.ORGANIZATION_ID);
        assertNull(taskId);
        assertNull(organizationId);
    }

    @Test
    public void testClear(){
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals("1", taskId);
        OnlineSchemaChangeContextHolder.clear();
        String taskId2 = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertNull(taskId2);
    }

    @Test
    public void testGetKey(){
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals("1", taskId);
        OnlineSchemaChangeContextHolder.clear();
    }
}