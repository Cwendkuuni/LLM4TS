
package com.oceanbase.odc.service.onlineschemachange;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {
    
    private static final String TASK_ID = "taskId";
    private static final String TASK_WORK_SPACE = "onlineSchemaChangeWorkSpace";
    private static final String ORGANIZATION_ID = "orgId";

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> contextMap = new HashMap<>();
        contextMap.put(TASK_WORK_SPACE, "1000");
        contextMap.put(TASK_ID, "2000");
        contextMap.put(OdcConstants.ORGANIZATION_ID, "3000");
        OnlineSchemaChangeContextHolder.retrace(contextMap);
        assertEquals("1000", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals("2000", OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals("3000", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
    }
}
