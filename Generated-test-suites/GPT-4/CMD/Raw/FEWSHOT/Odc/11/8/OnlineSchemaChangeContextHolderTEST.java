package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(12345L, 67890L, 111213L);

        String taskWorkSpace = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String orgId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);

        assertEquals("12345", taskWorkSpace);
        assertEquals("67890", taskId);
        assertEquals("111213", orgId);
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("7890", "12345", "67890");

        String taskWorkSpace = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String orgId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);

        assertEquals("7890", taskWorkSpace);
        assertEquals("12345", taskId);
        assertEquals("67890", orgId);

        OnlineSchemaChangeContextHolder.trace(null, null, null);
        
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("testKey", "testValue");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertNotNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        
        OnlineSchemaChangeContextHolder.clear();
        
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
       
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get("invalidKey"));
    }
}