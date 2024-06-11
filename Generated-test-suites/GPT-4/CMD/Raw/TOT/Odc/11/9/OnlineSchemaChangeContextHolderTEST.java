package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {
    private static final String TASK_ID_TEST = "1";
    private static final String USER_ID_TEST = "1";
    private static final String ORG_ID_TEST  = "1";

    private static final String TEST_KEY      = "TestKey";
    private static final String TEST_VALUE    = "TestValue";
    private static final Map<String, String> TEST_CONTEXT = Collections.singletonMap(TEST_KEY, TEST_VALUE);

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(Long.parseLong(USER_ID_TEST), Long.parseLong(TASK_ID_TEST), Long.parseLong(ORG_ID_TEST));
        assertEquals(USER_ID_TEST, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TASK_ID_TEST, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(ORG_ID_TEST, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace(USER_ID_TEST, TASK_ID_TEST, ORG_ID_TEST);
        assertEquals(USER_ID_TEST, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TASK_ID_TEST, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(ORG_ID_TEST, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(TEST_CONTEXT);
        assertEquals(TEST_VALUE, OnlineSchemaChangeContextHolder.get(TEST_KEY));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.retrace(TEST_CONTEXT);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(TEST_KEY));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.retrace(TEST_CONTEXT);
        assertEquals(TEST_VALUE, OnlineSchemaChangeContextHolder.get(TEST_KEY));
    }
}