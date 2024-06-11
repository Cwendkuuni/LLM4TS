import org.junit.*;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLong() {
        // Typical use case
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        Assert.assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals("789", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));

        // Edge case
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        // Typical use case
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        Assert.assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals("789", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));

        // Edge case
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        // Error scenario
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");
        context.put("key3", "value3");

        OnlineSchemaChangeContextHolder.retrace(context);
        Assert.assertEquals("value1", OnlineSchemaChangeContextHolder.get("key1"));
        Assert.assertEquals("value2", OnlineSchemaChangeContextHolder.get("key2"));
        Assert.assertEquals("value3", OnlineSchemaChangeContextHolder.get("key3"));
    }

    @Test
    public void testClear() {
        // Typical use case
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        OnlineSchemaChangeContextHolder.clear();
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }
}