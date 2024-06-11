package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder contextHolder;

    @Before
    public void setup() {
        contextHolder = new OnlineSchemaChangeContextHolder();
    }

    @Test
    public void testTraceString() {
        contextHolder.trace("1", "1", "1");
        assertEquals("1", contextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("1", contextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("1", contextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
    
    @Test
    public void testTraceLong() {
        contextHolder.trace(1L, 1L, 1L);
        assertEquals("1", contextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("1", contextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("1", contextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("taskId", "2");
        context.put("onlineSchemaChangeWorkSpace", "2");
        context.put(OdcConstants.ORGANIZATION_ID, "2");
        contextHolder.retrace(context);
        assertEquals("2", contextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", contextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("2", contextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        contextHolder.trace(1L, 1L, 1L);
        contextHolder.clear();
        assertNull(contextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(contextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(contextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test edge case when passing null to trace
    @Test(expected = NumberFormatException.class)
    public void testTraceNullStrings() {
        contextHolder.trace(null, null, null);
    }

    // Test edge case when passing invalid string to trace
    @Test(expected = NumberFormatException.class)
    public void testTraceInvalidStrings() {
        contextHolder.trace("invalid", "invalid", "invalid");
    }

    // Test edge case when retrace with null
    @Test(expected = NullPointerException.class)
    public void testRetraceNull() {
        contextHolder.retrace(null);
    }
    
}
