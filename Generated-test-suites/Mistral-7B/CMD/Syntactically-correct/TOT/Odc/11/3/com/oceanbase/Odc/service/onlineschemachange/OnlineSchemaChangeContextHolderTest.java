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

    private Map<String, String> context;

    @Before
    public void setUp() {
        context = new HashMap<>();
        MDC.setContextMap(context);
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongParameters() {
        // Given
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        // When
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        // Then
        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeChangeHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        // Given
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        // When
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        // Then
        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);
        assertEquals(userIdL.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullParameters() {
        // Given
        Long userId = null;
        Long taskId = null;
        Long organizationId = null;

        // When
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        // Then
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        // Given
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        // When
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        // Then
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        // Given
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        // When
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();

        // Then
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}