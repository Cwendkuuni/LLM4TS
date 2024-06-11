package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.*;

import org.junit.Test;

import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // add assertion for organizationId as well
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // add assertion for organizationId as well
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringWithInvalidInputs() {
        OnlineSchemaChangeContextHolder.trace("123abc", "456def", "789ghi");
    }

    @Test
    public void testRetrace() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("testKey", "testValue");
        OnlineSchemaChangeContextHolder.retrace(testMap);
        assertEquals("testValue", MDC.get("testKey"));
    }

    @Test
    public void testClear() {
        MDC.put("testKey", "testValue");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("testKey"));
    }

    @Test
    public void testGet() {
        MDC.put("testKey", "testValue");
        String value = OnlineSchemaChangeContextHolder.get("testKey");
        assertEquals("testValue", value);
    }
}