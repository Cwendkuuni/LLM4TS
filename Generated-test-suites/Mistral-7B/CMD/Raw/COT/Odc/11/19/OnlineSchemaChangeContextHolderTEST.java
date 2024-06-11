import org.junit.Assert;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongParameters() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.OdcConstants.ORGANIZATION_ID);
        Assert.assertEquals(userId.toString(), taskIdValue);
        Assert.assertEquals(organizationId.toString(), organizationIdValue);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.OdcConstants.ORGANIZATION_ID);
        Long userIdL = (userId == null ? null : Long.parseLong(userId));
        Long taskIdL = (taskId == null ? null : Long.parseLong(taskId));
        Long organizationIdL = (organizationId == null ? null : Long.parseLong(organizationId));
        Assert.assertEquals(userIdL.toString(), taskIdValue);
        Assert.assertEquals(organizationIdL.toString(), organizationIdValue);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithNullParameters() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.OdcConstants.ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidLongString() {
        OnlineSchemaChangeContextHolder.trace("abc", 2L, 3L);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeChangeHolder.retrace(context);
        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.OdcConstants.ORGANIZATION_ID);
        Assert.assertEquals("1", taskIdValue);
        Assert.assertEquals("3", organizationIdValue);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGetNullKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(null));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testGetNonExistentKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        Assert.assertNull(OnlineSchemaChangeContextHolder.get("nonExistentKey"));
        OnlineSchemaChangeContextHolder.clear();
    }
}