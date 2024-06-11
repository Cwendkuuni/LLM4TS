import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder.OdcConstants;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> context;
    private Long userId;
    private Long taskId;
    private Long organizationId;

    @Before
    public void setUp() {
        context = new HashMap<>();
        userId = 1L;
        taskId = 1L;
        organizationId = 1L;
    }

    @Test
    public void testTrace() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assertEquals(userId + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTraceIllegalArgument() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceInvalidNumberFormat() {
        OnlineSchemaChangeContextHolder.trace("invalidUserId", null, null);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceInvalidNumberFormatWithString() {
        OnlineSchemaChangeContextHolder.trace(null, "invalidTaskId", null);
    }

    @Test
    public void testTraceMultipleTimes() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = OnlineSchemaChangeContextHolder.getAllContext();
        assertEquals(userId + "", context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", context.get(OdcConstants.ORGANIZATION_ID));
        assertEquals(1, context.size());
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assertEquals(userId + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGetEmpty() {
        assertEquals(null, OnlineSchemaChangeContextHolder.get(new String("EmptyKey")));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = new HashMap<>();
        context.put("testKey", "testValue");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }

}