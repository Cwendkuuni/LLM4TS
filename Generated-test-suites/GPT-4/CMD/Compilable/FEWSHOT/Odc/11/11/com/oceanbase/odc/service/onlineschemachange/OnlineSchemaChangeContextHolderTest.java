package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongValues() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
   
        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
        // clear context
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithStringValues() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
        // clear context
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "workspace");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "4");
        context.put(OdcConstants.ORGANIZATION_ID, "5");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("workspace", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("4", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("5", MDC.get(OdcConstants.ORGANIZATION_ID));
        // clear context
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testClear() {
        // set some values
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        // clear context
        OnlineSchemaChangeContextHolder.clear();
        // all values should be null
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
    }
  
    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // clear context
        OnlineSchemaChangeContextHolder.clear();
    }
    
}