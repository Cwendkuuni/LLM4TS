package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace_WithLongParameters() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTrace_WithStringParameters() {
        OnlineSchemaChangeContextHolder.trace("4", "5", "6");
        assertEquals("4", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("5", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("6", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("value1", MDC.get("key1"));
    } 

    @Test
    public void testClear() {
        MDC.put("key", "value");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("key"));
    }

    @Test
    public void testGet() {
        MDC.put("key", "value");
        String value = OnlineSchemaChangeContextHolder.get("key");
        assertEquals("value", value);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTrace_NullInput() {
        OnlineSchemaChangeContextHolder.trace((Long) null, null, null);
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTrace_NonNumericStringInput() {
        OnlineSchemaChangeContextHolder.trace("abc", "def", "ghi");
    }
}