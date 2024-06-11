
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> context;

    @Before
    public void setUp() {
        context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");
    }

    @Test
    public void trace_withLongParams() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void trace_withStringParams() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void trace_withNullParams() {
        OnlineSchemaChangeContextHolder.trace((String) null, (String) null, (String) null);
        
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void retrace() {
        OnlineSchemaChangeContextHolder.retrace(context);
        
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void clear() {
        OnlineSchemaChangeContextHolder.clear();
        
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void get() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "test_workspace");
        
        assertEquals("test_workspace", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }
}
