
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {
    private static final String USER_ID = "123";
    private static final String TASK_ID = "456";
    private static final String ORG_ID = "789";

    @Before
    public void setUp() {
       MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongs() {
        OnlineSchemaChangeContextHolder.trace(Long.parseLong(USER_ID), Long.parseLong(TASK_ID), Long.parseLong(ORG_ID));
        
        assertEquals(USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(ORG_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStrings() {
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORG_ID);
        
        assertEquals(USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(ORG_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, USER_ID);
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, TASK_ID);
        context.put(OdcConstants.ORGANIZATION_ID, ORG_ID);
        
        OnlineSchemaChangeContextHolder.retrace(context);
        
        assertEquals(USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(ORG_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORG_ID);
        OnlineSchemaChangeContextHolder.clear();
        
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }
    
    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORG_ID);
        assertEquals(TASK_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
}
