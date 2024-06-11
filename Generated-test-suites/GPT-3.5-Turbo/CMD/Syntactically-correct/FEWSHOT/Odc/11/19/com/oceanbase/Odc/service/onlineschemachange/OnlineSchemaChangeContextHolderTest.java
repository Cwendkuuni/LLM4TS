package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithLongParams() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testTraceWithStringParams() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
        
        OnlineSchemaChangeContextHolder.retrace(OnlineSchemaChangeContextHolder.getMDCContextMap());
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get("organizationId"));
    }

}