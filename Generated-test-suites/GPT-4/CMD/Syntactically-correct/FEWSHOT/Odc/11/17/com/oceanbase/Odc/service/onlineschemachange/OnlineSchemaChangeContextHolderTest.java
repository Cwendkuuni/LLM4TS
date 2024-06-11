package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {
    
    @Test 
    public void testTraceWithOnlyLongParameters() {
        OnlineSchemaChangeContextHolder.trace(100L, 200L, 300L);
        assertEquals("100", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("200", MDC.get("taskId"));
        assertEquals("300", MDC.get(OdcConstants.ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();

    }


    @Test 
    public void testTraceWithOnlyStringParameters() {
        OnlineSchemaChangeContextHolder.trace("100", "200", "300");
        assertEquals("100", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("200", MDC.get("taskId"));
        assertEquals("300", MDC.get(OdcConstants.ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test 
    public void testRetrace() {
        Map<String, String> context = new HashMap<String, String>();
        context.put("onlineSchemaChangeWorkSpace", "400");
        context.put("taskId", "500");
        context.put(OdcConstants.ORGANIZATION_ID, "600");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("400", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("500", MDC.get("taskId"));
        assertEquals("600", MDC.get(OdcConstants.ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test 
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(100L, 200L, 300L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("onlineSchemaChangeWorkSpace"));
        assertNull(MDC.get("taskId"));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(100L, 200L, 300L);
        assertEquals("100", OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
        assertEquals("200", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("300", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();
    }
}