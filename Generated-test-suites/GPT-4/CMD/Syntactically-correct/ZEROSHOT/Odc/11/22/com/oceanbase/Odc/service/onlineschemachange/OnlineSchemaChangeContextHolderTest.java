
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongArguments() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        
        String userID = MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String taskID = MDC.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationID = MDC.get(OdcConstants.ORGANIZATION_ID);
        
        assertEquals("123", userID);
        assertEquals("456", taskID);
        assertEquals("789", organizationID);
    }

    @Test
    public void testTraceWithStringArguments() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        
        String userID = MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String taskID = MDC.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationID = MDC.get(OdcConstants.ORGANIZATION_ID);
        
        assertEquals("123", userID);
        assertEquals("456", taskID);
        assertEquals("789", organizationID);
    }

    @Test
    public void testRetrace() {
        Map<String, String> contextMap = new HashMap<String, String>();
        contextMap.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1000");
        contextMap.put(OnlineSchemaChangeContextHolder.TASK_ID, "2000");
        contextMap.put(OdcConstants.ORGANIZATION_ID, "3000");

        OnlineSchemaChangeContextHolder.retrace(contextMap);
        
        assertEquals("1000", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2000", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3000", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "567");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "890");
        MDC.put(OdcConstants.ORGANIZATION_ID, "123");
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        String taskWorkSpace = "567";
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, taskWorkSpace);
        assertEquals(taskWorkSpace, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));

        String taskId = "890";
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId);
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));

        String organizationId = "123";
        MDC.put(OdcConstants.ORGANIZATION_ID, organizationId);
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}
