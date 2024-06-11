package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.HashMap;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> map;

    @Before
    public void setup(){
        map = new HashMap<String, String>();
    }

    @Test
    public void testTrace() {
        OnlineSchemaChangeContextHolder.trace(1l, 2l, 3l);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
    
    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
    
    @Test
    public void testRetrace() {
        map.clear();
        map.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        map.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        OnlineSchemaChangeContextHolder.retrace(map);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
  
    @Test(expected = NumberFormatException.class)
    public void testTraceNumberFormatException() {
        OnlineSchemaChangeContextHolder.trace("1", "aaa", "3");
    }
}