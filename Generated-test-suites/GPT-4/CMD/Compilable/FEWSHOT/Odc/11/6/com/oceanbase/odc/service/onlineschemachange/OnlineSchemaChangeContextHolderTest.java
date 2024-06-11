package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.slf4j.MDC;
import com.oceanbase.odc.core.shared.constant.OdcConstants;

public class OnlineSchemaChangeContextHolderTest {
  
    @Test
    public void testTraceByLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
        MDC.clear();
    }
    
    @Test
    public void testTraceByString() {
        OnlineSchemaChangeContextHolder.trace("4", "5", "6");
        assertEquals("4", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("5", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("6", MDC.get(OdcConstants.ORGANIZATION_ID));
        MDC.clear();
    }
    
    @Test
    public void testRetrace() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "7");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "8");
        MDC.put(OdcConstants.ORGANIZATION_ID, "9");
        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());
        assertEquals("7", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("8", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("9", MDC.get(OdcConstants.ORGANIZATION_ID));
        MDC.clear();
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "10");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "11");
        String taskWorkSpace = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals("11", taskWorkSpace);
        MDC.clear();
    }
}