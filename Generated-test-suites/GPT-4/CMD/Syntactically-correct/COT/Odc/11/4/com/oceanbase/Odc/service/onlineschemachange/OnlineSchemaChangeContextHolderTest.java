package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTrace() {
        OnlineSchemaChangeContextHolder.trace(5L, 10L, 15L);
        assertEquals("5", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("10", MDC.get("taskId"));
        assertEquals("15", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStrings() {
        OnlineSchemaChangeContextHolder.trace("25", "27", "33");
        assertEquals("25", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("27", MDC.get("taskId"));
        assertEquals("33", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("onlineSchemaChangeWorkSpace", "100");
        testMap.put("taskId", "200");
        testMap.put(OdcConstants.ORGANIZATION_ID, "300");
        
        OnlineSchemaChangeContextHolder.retrace(testMap);

        assertEquals("100", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("200", MDC.get("taskId"));
        assertEquals("300", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        MDC.put("onlineSchemaChangeWorkSpace", "123");
        MDC.put("taskId", "456");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("onlineSchemaChangeWorkSpace"));
        assertNull(MDC.get("taskId"));
    }

    @Test
    public void testGet() {
        MDC.put("testKey", "testValue");
        assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }
}
