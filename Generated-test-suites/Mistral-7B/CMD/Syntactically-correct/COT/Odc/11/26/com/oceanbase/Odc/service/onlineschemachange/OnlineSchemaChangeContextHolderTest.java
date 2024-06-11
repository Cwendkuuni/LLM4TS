package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        MDC.clear();
    }

    // Test case for trace method with valid parameters
    @Test
    public void testTraceWithValidParameters() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId = OnlineSchemaChangeChangeHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals("2", taskId);
        assertEquals("3", organizationId);
    }

    // Test case for trace method with null parameters
    @Test
    public void testTraceWithNullParameters() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertNull(taskId);
        assertNull(organizationId);
    }

    // Test case for trace method with String parameters
    @Test
    public void testTraceWithStringParameters() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        Long userId = Long.parseLong("1");
        Long taskIdL = Long.parseLong("2");
        Long organizationIdL = Long.parseLong("3");
        assertEquals(userId.toString(), taskId);
        assertEquals(organizationIdL.toString(), organizationId);
    }

    // Test case for retrace method
    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        MDC.get(OnlineSchemaChangeContextHolder.TASK_ID); // should throw NoSuchElementException
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(context);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals("2", taskId);
    }

    // Test case for clear method
    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertNull(taskId);
    }
}