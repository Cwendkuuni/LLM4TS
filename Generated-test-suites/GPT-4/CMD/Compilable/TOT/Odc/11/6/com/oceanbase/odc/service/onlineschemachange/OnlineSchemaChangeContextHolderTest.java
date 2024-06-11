package com.oceanbase.odc.service.onlineschemachange;


import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {
    private final static String userId = "1";
    private final static String taskId = "2";
    private final static String organizationId = "3";
  
    private final static String invalidValue = "invalid";

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test 
    public void testTraceLongInputs() {
        OnlineSchemaChangeContextHolder.trace(Long.parseLong(userId), Long.parseLong(taskId), Long.parseLong(organizationId));
        Assert.assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringInputs() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Assert.assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceInvalidStringInputs() {
        OnlineSchemaChangeContextHolder.trace(invalidValue, invalidValue, invalidValue);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId);
        OnlineSchemaChangeContextHolder.retrace(context);
        Assert.assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();
        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Assert.assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    // Additional tests can be added for edge cases and error handling scenarios.
}

