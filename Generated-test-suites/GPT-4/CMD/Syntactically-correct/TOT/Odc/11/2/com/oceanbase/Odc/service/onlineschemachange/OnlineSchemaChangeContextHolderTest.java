package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(12345L, 67890L, 11111L);
        assertEquals("12345", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("67890", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("11111", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("12345", "67890", "11111");
        assertEquals("12345", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("67890", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("11111", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "54321");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "98765");
        context.put(OdcConstants.ORGANIZATION_ID, "11111");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("54321", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("98765", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("11111", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "12345");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "67890");
        MDC.put(OdcConstants.ORGANIZATION_ID, "11111");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "12345");
        assertEquals("12345", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @After
    public void clearContext() {
        MDC.clear();
    }
}