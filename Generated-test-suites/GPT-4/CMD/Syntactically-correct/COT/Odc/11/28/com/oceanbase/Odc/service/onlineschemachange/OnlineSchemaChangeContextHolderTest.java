package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

public class OnlineSchemaChangeContextHolderTest {
    private static Long userId = 12345L;
    private static Long taskId = 67890L;
    private static Long orgId = 13579L;

    @Before
    public void beforeEachTest() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);
        Assert.assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals(orgId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace(userId.toString(), taskId.toString(), orgId.toString());
        Assert.assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals(orgId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("key", "value");
        OnlineSchemaChangeContextHolder.retrace(testMap);
        Assert.assertEquals("value", MDC.get("key"));
    }

    @Test
    public void testGet() {
        MDC.put("key", "value");
        Assert.assertEquals("value", OnlineSchemaChangeContextHolder.get("key"));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringInvalidNumbers() {
        OnlineSchemaChangeContextHolder.trace("abc", "def", "ghi");
    }

    @Test
    public void testTraceStringNull() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        MDC.put("key", "value");
        OnlineSchemaChangeContextHolder.clear();
        Assert.assertNull(MDC.get("key"));
    }

    @Test
    public void testGetNonExisting() {
        Assert.assertNull(OnlineSchemaChangeContextHolder.get("non-existing-key"));
    }
}
