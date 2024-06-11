package com.oceanbase.odc.service.onlineschemachange;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.*;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLongVersion() {
        OnlineSchemaChangeContextHolder.trace(100L, 200L, 300L);
        assertEquals("100", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("200", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
    
    @Test
    public void testTraceLongVersionNullValues() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTraceStringVersion() {
        OnlineSchemaChangeContextHolder.trace("100", "200", "300");
        assertEquals("100", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("200", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
    
    @Test
    public void testTraceStringVersionNullValues() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testRetraceEmptyContext() {
        Map<String, String> emptyContext = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(emptyContext);
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testGetNonExistentKey() {
        assertNull(OnlineSchemaChangeContextHolder.get("nonExistentKey"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(100L, 200L, 300L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
}
