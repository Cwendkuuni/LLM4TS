package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLongParameters() {
        final long userId = 10L;
        final long taskId = 100L;
        final long orgId = 1000L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        Assert.assertEquals(Long.toString(userId), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(Long.toString(taskId), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals(Long.toString(orgId), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringParameters() {
        final String userId = "1";
        final String taskId = "10";
        final String orgId = "100";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        Assert.assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals(orgId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        final Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1111");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2222");
        context.put(OdcConstants.ORGANIZATION_ID, "3333");
        OnlineSchemaChangeContextHolder.retrace(context);

        Assert.assertEquals("1111", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("2222", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals("3333", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        OnlineSchemaChangeContextHolder.clear();
        
        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }
    
    @Test
    public void testGet() {
        final String userId = "123";
        final String taskId = "456";
        
        OnlineSchemaChangeContextHolder.trace(userId, taskId, "789");
        
        Assert.assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
}