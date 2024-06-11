
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.*;
import org.slf4j.MDC;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLong() {
        Long userId = 1L;
        Long taskId = 1L;
        Long organizationId = 1L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceString() {
        String userId = "1";
        String taskId = "1";
        String organizationId = "1";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringError() {
        String userId = "abc";
        String taskId = "abc";
        String organizationId = "abc";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test
    public void testRetrace() {
        MDC.put("key", "value");

        assertEquals("value", MDC.get("key"));

        MDC.clear();
        assertNull(MDC.get("key"));
        
        OnlineSchemaChangeContextHolder.retrace(Map.of("key", "value"));
        assertEquals("value", MDC.get("key"));
        
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testClear() {
        MDC.put("key", "value");

        assertEquals("value", MDC.get("key"));

        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get("key"));
    }

    @Test
    public void testGet() {
        MDC.put("key", "value");

        assertEquals("value", MDC.get("key"));

        assertEquals("value", OnlineSchemaChangeContextHolder.get("key"));

        OnlineSchemaChangeContextHolder.clear();
    }
}
