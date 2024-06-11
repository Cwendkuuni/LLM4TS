package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        String userId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals("2", userId);
        assertEquals("1", taskId);
        assertEquals("3", organizationId);
    }

    // Testing null values
    @Test(expected = NullPointerException.class)
    public void testTraceNullUserId() {
        OnlineSchemaChangeContextHolder.trace(null, 2L, 3L);
    }

    @Test(expected = NullPointerException.class)
    public void testTraceNullTaskId() {
        OnlineSchemaChangeContextHolder.trace(1L, null, 3L);
    }

    @Test(expected = NullPointerException.class)
    public void testTraceNullOrganizationId() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, null);
    }

    // Testing empty strings
    @Test
    public void testTraceEmptyStrings() {
        OnlineSchemaChangeContextHolder.trace("", "", "");
        String userId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals("", userId);
        assertEquals("", taskId);
        assertEquals("", organizationId);
    }

    // Testing large strings
    @Test
    public void testTraceLargeStrings() {
        final String largeString = "1234567890123456789012345678901234567890";
        OnlineSchemaChangeContextHolder.trace(1L, largeString, largeString);
        String userId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(largeString, userId);
        assertEquals(largeString, taskId);
        assertEquals(largeString, organizationId);
    }

    // Testing negative numbers
    @Test(expected = IllegalArgumentException.class)
    public void testTraceNegativeNumbers() {
        OnlineSchemaChangeContextHolder.trace(-1L, -2L, -3L);
    }

    // Testing setting the context with an existing context
    @Test
    public void testSetContextWithExistingContext() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        Map<String, String> context = new HashMap<>();
        context.putAll(Arrays.asList(
            OnlineSchemaChangeContextHolder.TASK_ID + "=" + "1",
            OnlineSchemaChangeContextHolder.TASK_WORK_SPACE + "=" + "2",
            OdcConstants.ORGANIZATION_ID + "=" + "3"
        ).toArray(new String[0]));
        OnlineSchemaChangeContextHolder.retrace(context);
        String userId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals("1", userId);
        assertEquals("1", taskId);
        assertEquals("3", organizationId);
    }

    // Testing clearing the context without any data
    @Test
    public void testClearContextWithoutData() {
        OnlineSchemaChangeContextHolder.clear();
        String userId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(null, userId);
        assertEquals(null, taskId);
        assertEquals(null, organizationId);
    }
}