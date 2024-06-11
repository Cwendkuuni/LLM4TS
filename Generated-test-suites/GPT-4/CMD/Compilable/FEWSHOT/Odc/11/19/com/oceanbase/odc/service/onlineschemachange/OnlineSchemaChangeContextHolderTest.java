package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", MDC.get("organizationId"));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("10", "20", "30");
        assertEquals("10", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("20", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("30", MDC.get("organizationId"));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "100");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "200");
        context.put("organizationId", "300");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("100", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("200", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("300", MDC.get("organizationId"));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get("organizationId"));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertNotNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNotNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNotNull(OnlineSchemaChangeContextHolder.get("organizationId"));
        OnlineSchemaChangeContextHolder.clear();
    }
}