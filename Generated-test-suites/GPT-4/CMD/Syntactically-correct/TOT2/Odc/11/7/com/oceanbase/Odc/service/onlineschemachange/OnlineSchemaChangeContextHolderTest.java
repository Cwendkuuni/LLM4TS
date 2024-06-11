
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {
  
    private static final String TASK_ID = "taskId";
    private static final String TASK_WORK_SPACE = "onlineSchemaChangeWorkSpace";


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(TASK_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(TASK_ID));
    }

    @Test
    public void testTraceStringNull() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(TASK_WORK_SPACE, "test");
        context.put(TASK_ID, "test");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("test", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals("test", OnlineSchemaChangeContextHolder.get(TASK_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
    }
  

    // Exception cases
    @Test
    public void testTraceStringInvalidNumberFormat() {
        expectedException.expect(NumberFormatException.class);
        OnlineSchemaChangeContextHolder.trace("invalid", "invalid", "invalid");
    }

    @Test
    public void testGetKeyNotFound() {
        assertNull(OnlineSchemaChangeContextHolder.get("nonExistKey"));
    }
}
