package com.oceanbase.odc.service.onlineschemachange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithLongParameters() {
        OnlineSchemaChangeContextHolder.trace(100L, 200L, 300L);

        assertEquals("100", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("200", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("300", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        OnlineSchemaChangeContextHolder.trace("400", "500", "600");

        assertEquals("400", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("500", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("600", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParametersNulls() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "700");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "800");
        context.put(OdcConstants.ORGANIZATION_ID, "900");
        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("700", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("800", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("900", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }
}
