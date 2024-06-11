package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private static final Long TEST_USER_ID = 1234L;
    private static final Long TEST_TASK_ID = 5678L;
    private static final Long TEST_ORG_ID = 90L;

    @Before
    public void setup() {
        MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORG_ID);
        assertEquals(TEST_USER_ID.toString(), MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals(TEST_TASK_ID.toString(), MDC.get("taskId"));
        assertEquals(TEST_ORG_ID.toString(), MDC.get("organizationId"));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace(TEST_USER_ID.toString(), TEST_TASK_ID.toString(), TEST_ORG_ID.toString());
        assertEquals(TEST_USER_ID.toString(), MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals(TEST_TASK_ID.toString(), MDC.get("taskId"));
        assertEquals(TEST_ORG_ID.toString(), MDC.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("onlineSchemaChangeWorkSpace", TEST_USER_ID.toString());
        context.put("taskId", TEST_TASK_ID.toString());
        context.put("organizationId", TEST_ORG_ID.toString());

        MDC.setContextMap(context);
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(TEST_USER_ID.toString(), MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals(TEST_TASK_ID.toString(), MDC.get("taskId"));
        assertEquals(TEST_ORG_ID.toString(), MDC.get("organizationId"));
    }

    @Test
    public void testClear() {
        MDC.put("onlineSchemaChangeWorkSpace", "test");
        MDC.put("taskId", "test");
        MDC.put("organizationId", "test");

        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("onlineSchemaChangeWorkSpace"));
        assertNull(MDC.get("taskId"));
        assertNull(MDC.get("organizationId"));
    }

    @Test
    public void testGet() {
        MDC.put("testKey", "testValue");
        
        assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }
}