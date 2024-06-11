package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongParameters() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        String userIdFromContext = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskId.toString(), userIdFromContext);
        String organizationIdFromContext = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeConstants.ORGANIZATION_ID);
        assertEquals(organizationId.toString(), organizationIdFromContext);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        String userIdFromContext = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskId, userIdFromContext);
        String organizationIdFromContext = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID);
        assertEquals(organizationId, organizationIdFromContext);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidLongParameters() {
        Long userId = null;
        Long taskId = null;
        Long organizationId = null;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        String userIdFromContext = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdFromContext = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID);
        assertEquals(null, userIdFromContext);
        assertEquals(null, organizationIdFromContext);
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(context);
        String userIdFromContext2 = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdFromContext2 = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID);
        assertEquals("2", userIdFromContext2);
        assertEquals("3", organizationIdFromContext2);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        String userIdFromContext = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationIdFromContext = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID);
        assertEquals("2", userIdFromContext);
        assertEquals("3", organizationIdFromContext);
        OnlineSchemaChangeContextHolder.clear();
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ORGANIZATION_ID));
    }

    @Test
    public void testGetNullKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals(null, OnlineSchemaChangeContextHolder.get(null));
    }

    @Test
    public void testGetUnknownKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals(null, OnlineSchemaChangeContextHolder.get("unknownKey"));
    }

}