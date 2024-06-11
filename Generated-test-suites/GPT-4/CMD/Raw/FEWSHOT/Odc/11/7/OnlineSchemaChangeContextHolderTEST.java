package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.slf4j.MDC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
        //Clear context after test
        MDC.clear();
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
        //Clear context after test
        MDC.clear();
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>() {{
            put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123");
            put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
            put(OdcConstants.ORGANIZATION_ID, "789");
        }};
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
        //Clear context after test
        MDC.clear();
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        String key = OnlineSchemaChangeContextHolder.TASK_WORK_SPACE;
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get(key));
        //Clear context after test
        MDC.clear();
    }
}