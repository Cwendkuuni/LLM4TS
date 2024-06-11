package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        MDC.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), "1");
        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), "2");
        assertEquals(MDC.get(OdcConstants.ORGANIZATION_ID), "3");
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");

        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), "1");
        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), "2");
        assertEquals(MDC.get(OdcConstants.ORGANIZATION_ID), "3");
    }

    @Test
    public void testRetrace() {
        Map<String, String> contextMap = MDC.getMDCAdapter().getCopyOfContextMap();
        contextMap.put("sampleKey", "sampleValue");
        OnlineSchemaChangeContextHolder.retrace(contextMap);

        assertEquals(MDC.get("sampleKey"), "sampleValue");
    }

    @Test
    public void testClear() {
        MDC.put("something", "value");
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get("something"));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");
        String result = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);

        assertEquals(result, "1");
    }

    @After
    public void tearDown() {
        MDC.clear();
    }
}