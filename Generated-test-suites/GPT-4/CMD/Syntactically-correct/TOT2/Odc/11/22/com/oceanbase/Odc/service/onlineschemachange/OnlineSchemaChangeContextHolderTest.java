package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {
    
    @Before
    public void setUp() throws Exception {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // Add validation for the third value
    }

    @Test
    public void testTraceString() {
       OnlineSchemaChangeContextHolder.trace("123", "456", "789");
       assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
       assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // Add validation for the third value
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("value1", OnlineSchemaChangeContextHolder.get("key1"));
    }


    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // Add validation for the third value
    }

    @Test
    public void testGet() {
         OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
         assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
         assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
         // Add validation for the third value
    }
}