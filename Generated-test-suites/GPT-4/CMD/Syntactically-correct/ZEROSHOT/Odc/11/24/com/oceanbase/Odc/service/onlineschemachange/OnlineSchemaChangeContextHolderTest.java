package com.oceanbase.odc.service.onlineschemachange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.MDC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeContextHolderTest {

    private static final String TASK_ID = "taskId";
    private static final String TASK_WORK_SPACE = "onlineSchemaChangeWorkSpace";
    private static final String ORGANIZATION_ID = "organizationId";

    @Before
    public void setUp() {
        // Calling clear() to ensure no lingering data from previous tests
        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void tearDown() {
        // Calling clear() to clean up data after tests
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithLongParameters() {
        Long userId = 1234L;
        Long taskId = 5678L;
        Long organizationId = 91011L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1234";
        String taskId = "5678";
        String organizationId = "91011";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, MDC.get(TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(TASK_ID));
        assertEquals(organizationId, MDC.get(ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        MDC.put("key1", "value1");
        MDC.put("key2", "value2");

        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());

        assertEquals("value1", MDC.get("key1"));
        assertEquals("value2", MDC.get("key2"));
    }

    @Test
    public void testClear() {
        MDC.put("someKey", "someValue");

        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get("someKey"));
    }

    @Test
    public void testGet() {
        String key = "aKey";
        String value = "aValue";
        MDC.put(key, value);

        assertEquals(value, OnlineSchemaChangeContextHolder.get(key));
    }
}
