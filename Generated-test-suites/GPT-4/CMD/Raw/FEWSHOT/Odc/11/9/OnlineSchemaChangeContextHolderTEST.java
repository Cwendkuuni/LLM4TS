
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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
    public void testTraceLong() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        
        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }
    
    @Test
    public void testTraceString() {
        String userId = "4";
        String taskId = "5";
        String organizationId = "6";
        
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        
        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");
        
        OnlineSchemaChangeContextHolder.retrace(context);
    
        assertEquals("value1", MDC.get("key1"));
        assertEquals("value2", MDC.get("key2"));
    }

    @Test
    public void testClear() {
        String key = "key";
        String value = "value";
        MDC.put(key, value);
        
        OnlineSchemaChangeContextHolder.clear();
    
        assertNull(MDC.get(key));
    }

    @Test
    public void testGet() {
        String key = "key";
        String value = "value";
        MDC.put(key, value);

        String result = OnlineSchemaChangeContextHolder.get(key);
        
        assertEquals(value, result);
    }
}
