
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void cleanup() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void traceTestWithLongIds() {
        Long userId = 1234L, taskId = 5678L, organizationId = 91011L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Assert.assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void traceTestWithStringIds() {
        String userId = "1234", taskId = "5678", organizationId = "91011";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Assert.assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void retraceTest() {
        Map<String, String> context = new HashMap<>();
        context.put("testKey", "testValue");
        OnlineSchemaChangeContextHolder.retrace(context);
        Assert.assertEquals("testValue", MDC.get("testKey"));
    }

    @Test
    public void clearTest() {
        String userId = "1234", taskId = "5678", organizationId = "91011";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();
        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void getTest() {
        String userId = "1234";
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId);
        Assert.assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }
}
