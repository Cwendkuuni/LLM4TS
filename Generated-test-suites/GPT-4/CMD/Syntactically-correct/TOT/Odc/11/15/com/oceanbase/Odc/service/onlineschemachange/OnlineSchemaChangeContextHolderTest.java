package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {
    private static final String TASK_ID = "taskId";
    private static final String TASK_WORK_SPACE = "onlineSchemaChangeWorkSpace";
    private static final String ORGANIZATION_ID = "organizationId";

    @Before
    public void setup(){
       OnlineSchemaChangeContextHolder.clear(); // ensure clean environment before each test
    } 

    @Test
    public void testTraceWithLongParams() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        // Verify MDC values
        assertEquals(userId.toString(), MDC.get(TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(TASK_ID));
        // Assuming ORGANIZATION_ID is a static final variable in the OdcConstants, referring a string "organizationId"
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParams() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        // Verify MDC values
        assertEquals(userId, MDC.get(TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(TASK_ID));
        // Assuming ORGANIZATION_ID is a static final variable in the OdcConstants, referring a string "organizationId"        
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }
    
    @Test
    public void testRetrace(){
        HashMap<String, String> map = new HashMap<>();
        map.put(TASK_WORK_SPACE, "1");
        OnlineSchemaChangeContextHolder.retrace(map);
        assertEquals("1", MDC.get(TASK_WORK_SPACE));
    }

    @Test
    public void testClear() {
        String key = "key";
        String value = "value";
        MDC.put(key, value);
        assertEquals(value, MDC.get(key));
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(key));
    }

    @Test
    public void testGetWhenKeyExists() {
        String key = "key";
        String value = "value";
        MDC.put(key, value);
        assertEquals(value, OnlineSchemaChangeContextHolder.get(key));
    }
    
    @Test
    public void testGetWhenKeyDoesNotExist() {
        assertNull(OnlineSchemaChangeContextHolder.get("non-existing-key"));
    }
}