package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.HashMap;

public class OnlineSchemaChangeContextHolderTest{
  
    @Test
    public void traceLongTest(){
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));   
    }
    
    @Test
    public void traceStringTest(){
        OnlineSchemaChangeContextHolder.trace("789", "456", "123");
        assertEquals("789", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("123", MDC.get(OdcConstants.ORGANIZATION_ID));   
    }

    @Test
    public void retraceTest(){
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
        context.put(OdcConstants.ORGANIZATION_ID, "789");
        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));   
    }

    @Test
    public void clearTest(){
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));   
    }

    @Test
    public void getTest(){
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}