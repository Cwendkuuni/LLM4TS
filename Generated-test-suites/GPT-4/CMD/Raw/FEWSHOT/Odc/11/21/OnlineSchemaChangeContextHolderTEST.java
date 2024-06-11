package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    private static final String TEST_KEY = "Key";
    private static final String TEST_VAL = "Value";

    private Long userIdL;
    private Long taskIdL;
    private Long organizationIdL;

    @Before
    public void setUp() {
        userIdL = 1L;
        taskIdL = 2L;
        organizationIdL = 3L;

        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(userIdL, taskIdL, organizationIdL);
        assertEquals(userIdL.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTraceString() {
        String userIdS = "4";
        String taskIdS = "5";
        String organizationIdS = "6";

        OnlineSchemaChangeContextHolder.trace(userIdS, taskIdS, organizationIdS);
        assertEquals(userIdS, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdS, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<String, String>();
        context.put(TEST_KEY, TEST_VAL);
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(TEST_VAL, OnlineSchemaChangeContextHolder.get(TEST_KEY));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
    }

    @Test
    public void testGet() {
        MDC.put(TEST_KEY, TEST_VAL);
        assertEquals(TEST_VAL, OnlineSchemaChangeContextHolder.get(TEST_KEY));
        assertEquals(null, OnlineSchemaChangeContextHolder.get("Non-existent key"));
    }
}