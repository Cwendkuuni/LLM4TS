package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private static final String TEST_USER_ID = "123";
    private static final String TEST_TASK_ID = "456";
    private static final String TEST_ORGANIZATION_ID = "789";

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithLongs() {
        OnlineSchemaChangeContextHolder.trace(Long.parseLong(TEST_USER_ID), Long.parseLong(TEST_TASK_ID), Long.parseLong(TEST_ORGANIZATION_ID));
        assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(TEST_ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStrings() {
        OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORGANIZATION_ID);
        assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(TEST_ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> map = new HashMap<>();
        map.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, TEST_USER_ID);
        map.put(OnlineSchemaChangeContextHolder.TASK_ID, TEST_TASK_ID);
        map.put(OdcConstants.ORGANIZATION_ID, TEST_ORGANIZATION_ID);
        OnlineSchemaChangeContextHolder.retrace(map);
        assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(TEST_ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, TEST_TASK_ID);
        assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, TEST_TASK_ID);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

}