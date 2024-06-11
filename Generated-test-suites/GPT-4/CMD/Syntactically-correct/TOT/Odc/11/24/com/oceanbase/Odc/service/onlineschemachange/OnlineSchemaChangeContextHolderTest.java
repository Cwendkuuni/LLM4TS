package com.oceanbase.odc.service.onlineschemachange;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        // before each test, clear the MDC to ensure a clean environment
        MDC.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        //assert whatever the "OdcConstants.ORGANIZATION_ID" contains
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("4", "5", "6");
        assertEquals("4", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("5", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        //assert whatever the "OdcConstants.ORGANIZATION_ID" contains
        assertEquals("6", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString_Null() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        //assert whatever the "OdcConstants.ORGANIZATION_ID" contains
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> contextMap = new HashMap<>();
        contextMap.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "7");
        contextMap.put(OnlineSchemaChangeContextHolder.TASK_ID, "8");
        contextMap.put(OdcConstants.ORGANIZATION_ID, "9");
        OnlineSchemaChangeContextHolder.retrace(contextMap);
        assertEquals("7", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("8", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        //assert whatever the "OdcConstants.ORGANIZATION_ID" contains
        assertEquals("9", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClearThenGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        //assert whatever the "OdcConstants.ORGANIZATION_ID" contains
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @After
    public void teardown() {
        // ensure the MDC is cleared after each test
        MDC.clear();
    }
}