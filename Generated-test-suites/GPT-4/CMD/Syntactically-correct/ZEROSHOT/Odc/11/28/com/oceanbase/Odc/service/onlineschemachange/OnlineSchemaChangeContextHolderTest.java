package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {
    
    private static final String TEST_USER_ID = "100";
    private static final String TEST_TASK_ID = "200";
    private static final String TEST_ORGANIZATION_ID = "300";
    
    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }
    
    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(Long.valueOf(TEST_USER_ID), Long.valueOf(TEST_TASK_ID), Long.valueOf(TEST_ORGANIZATION_ID));
        assertEquals(TEST_USER_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(TEST_ORGANIZATION_ID, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
    
    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORGANIZATION_ID);
        assertEquals(TEST_USER_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(TEST_ORGANIZATION_ID, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
    
    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, TEST_USER_ID);
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, TEST_TASK_ID);
        context.put(OdcConstants.ORGANIZATION_ID, TEST_ORGANIZATION_ID);

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(TEST_ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }
  
    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORGANIZATION_ID);
        assertNotNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNotNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNotNull(MDC.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }
  
    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }
}
