package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

import static org.junit.Assert.*;

import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTrace_Long() {
        Long userId = 1234L;
        Long taskId = 5678L;
        Long organizationId = 91011L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTrace_String() {
        String userId = "1234";
        String taskId = "5678";
        String organizationId = "91011";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTrace_String_Exception() {
        String userId = "abc";
        OnlineSchemaChangeContextHolder.trace(userId, "123", "123");
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("value1", MDC.get("key1"));
    }

    @Test(expected = NullPointerException.class)
    public void testRetrace_NullContext() {
        OnlineSchemaChangeContextHolder.retrace(null);
    }

    @Test
    public void testClear() {
        MDC.put("key1", "value1");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("key1"));
    }

    @Test
    public void testGet() {
        MDC.put("key1", "value1");
        assertEquals("value1", OnlineSchemaChangeContextHolder.get("key1"));
    }
    @Test(expected = NullPointerException.class)
    public void testGet_NullKey() {
        OnlineSchemaChangeContextHolder.get(null);
    }
}