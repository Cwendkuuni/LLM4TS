
package com.oceanbase.odc.service.onlineschemachange;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {
    
    @Before
    public void setUp() {
        MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }
    
    @Test
    public void testTraceLongParameters() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringParameters() {
        OnlineSchemaChangeContextHolder.trace("10", "20", "30");
        assertEquals("10", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("20", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("30", MDC.get(OdcConstants.ORGANIZATION_ID));
    }
    
    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("10", "20", "30");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }
    
    @Test
    public void testRetrace() {
        Map<String, String> testContext = new HashMap<>();
        testContext.put("testKey", "testValue");
        OnlineSchemaChangeContextHolder.retrace(testContext);
        assertEquals("testValue", MDC.get("testKey"));
    }

    @Test
    public void testGet() {
        MDC.put("testKey", "testValue");
        assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }
}
