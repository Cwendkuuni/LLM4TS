package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    private static final String USER_ID = "1";
    private static final String TASK_ID = "1";
    private static final String ORGANIZATION_ID = "1";
    private static final String TASK_WORK_SPACE = "onlineSchemaChangeWorkSpace";


    @Test
    public void traceTestLong() {
        Long userId = 1L;
        Long taskId = 1L;
        Long organizationId = 1L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE), USER_ID);
        assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID), TASK_ID);
        assertEquals(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID), ORGANIZATION_ID);
    }

    @Test
    public void traceTestString() {
        
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);

        assertEquals(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE), USER_ID);
        assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID), TASK_ID);
        assertEquals(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID), ORGANIZATION_ID);
    }

    @Test
    public void retraceTest() {
        Map<String, String> map = new HashMap<>();
        map.put(TASK_WORK_SPACE, USER_ID);
        map.put(OnlineSchemaChangeContextHolder.TASK_ID, TASK_ID);
        map.put(OdcConstants.ORGANIZATION_ID, ORGANIZATION_ID);

        OnlineSchemaChangeContextHolder.retrace(map);

        assertEquals(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE), USER_ID);
        assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID), TASK_ID);
        assertEquals(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID), ORGANIZATION_ID);
    }

    @Test
    public void clearTest() {
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);
        OnlineSchemaChangeContextHolder.clear();
        
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
   
    @Test
    public void getTest() {
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);
        
        assertEquals(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE), USER_ID);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
    }
}

