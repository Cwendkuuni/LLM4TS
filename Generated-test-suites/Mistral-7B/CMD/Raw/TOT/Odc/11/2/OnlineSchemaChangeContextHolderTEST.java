package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private static final String USER_ID = "123";
    private static final String TASK_ID = "456";
    private static final String ORGANIZATION_ID = "789";
    private static final String KEY = "testKey";

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongValues() {
        OnlineSchemaChangeContextHolder.trace(Long.parseLong(USER_ID), Long.parseLong(TASK_ID), Long.parseLong(ORGANIZATION_ID));
        Map<String, String> context = OnlineSchemaChangeContextHolder.getAllContext();
        assertEquals(USER_ID, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TASK_ID, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(ORGANIZATION_ID, context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringValues() {
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);
        Map<String, String> context = OnlineSchemaChangeContextHolder.getAllContext();
        assertEquals(USER_ID, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TASK_ID, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(ORGANIZATION_ID, context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, TASK_ID);
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(context, OnlineSchemaChangeContextHolder.getAllContext());
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(Long.parseLong(USER_ID), Long.parseLong(TASK_ID), Long.parseLong(ORGANIZATION_ID));
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(Long.parseLong(USER_ID), Long.parseLong(TASK_ID), Long.parseLong(ORGANIZATION_ID));
        assertEquals(TASK_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTraceWithNullValues() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        Map<String, String> context = OnlineSchemaChangeContextHolder.getAllContext();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testGetWithNullKey() {
        OnlineSchemaChangeContextHolder.trace(Long.parseLong(USER_ID), Long.parseLong(TASK_ID), Long.parseLong(ORGANIZATION_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(KEY));
    }

    @After
    public void tearDown() {
        MDC.clear();
    }
}