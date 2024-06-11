package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;

import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {
    
    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }
    
    @Test
    public void testTraceWithLongParameters() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
    
    @Test
    public void testTraceWithStringParameters() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
    
    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
    
    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
    
    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
    
    @Test(expected = NumberFormatException.class)
    public void testTraceInvalidStringInput() {
        OnlineSchemaChangeContextHolder.trace("INVALID_NUMBER", "2", "3");
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }
}