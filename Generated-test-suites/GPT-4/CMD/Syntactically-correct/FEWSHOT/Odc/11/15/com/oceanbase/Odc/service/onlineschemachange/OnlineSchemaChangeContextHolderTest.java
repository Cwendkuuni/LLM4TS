
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {
    @Before
    public void setup() {
        MDC.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(23L, 345L, 456L);

        assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), "23");
        assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID), "345");
        assertEquals(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID), "456");
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("23", "345", "456");

        assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), "23");
        assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID), "345");
        assertEquals(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID), "456");
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key", "value");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(OnlineSchemaChangeContextHolder.get("key"), "value");
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(23L, 345L, 456L);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put("testKey", "testValue");
        assertEquals(OnlineSchemaChangeContextHolder.get("testKey"), "testValue");
    }
}
