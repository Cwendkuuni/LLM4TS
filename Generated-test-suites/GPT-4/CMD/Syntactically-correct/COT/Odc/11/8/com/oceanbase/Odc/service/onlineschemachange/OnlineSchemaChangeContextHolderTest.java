package com.oceanbase.odc.service.onlineschemachange;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // you have to define the constant value of ORGANIZATION_ID
        assertEqual("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("4", "5", "6");
        assertEquals("4", MDC.get(OnlineSchemaChangeContextHolder));
        assertEquals("5", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // you have to define the constant value of ORGANIZATION_ID
        assertEqual("6", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringInvalidNumber() {
        OnlineSchemaChangeContextHolder.trace("invalidUserId", "invalidTaskId", "invalidOrganizationId");
    }

    @Test
    public void testRetrace() {
      // this test would needs to be improved based on specific business requirements
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("value1", MDC.get("key1"));
        assertEquals("value2", MDC.get("key2"));
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
        assertEquals("value", OnlineSchemaChangeContextHolder.get("key"));
    }

}