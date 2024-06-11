package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private static final Long USER_ID = 1L;
    private static final Long TASK_ID = 2L;
    private static final Long ORGANIZATION_ID = 3L;

    @Before
    public void resetMDC() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongArguments() {
        // Given
        Long userId = USER_ID;
        Long taskId = TASK_ID;
        Long organizationId = ORGANIZATION_ID;

        // When
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        // Then
        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));

        // Clean up
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithStringArguments() {
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
        assertEquals(userIdL.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));

        // Clean up
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithNullArguments() {
        // Given
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        // Then
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));

        // Clean up
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testRetrace() {
        // Given
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        // When
        OnlineSchemaChangeContextHolder.retrace(context);

        // Then
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        assertEquals(context, mdcContext);

        // Clean up
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testClear() {
        // Given
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);

        // When
        OnlineSchemaChangeContextHolder.clear();

        // Then
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        // Given
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);

        // When
        String value = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);

        // Then
        assertEquals(TASK_ID.toString(), value);

        // Clean up
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testGetNull() {
        // Given
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);

        // When
        String value = OnlineSchemaChangeContextHolder.get("invalidKey");

        // Then
        assertNull(value);

        // Clean up
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithMockedSecurityContextUtils() {
        // Given
        Long userId = USER_ID;
        Long taskId = TASK_ID;
        Long organizationId = ORGANIZATION_ID;

        Map<String, String> expectedContext = new HashMap<>();
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId.toString());
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId.toString());
        expectedContext.put(OdcConstants.ORGANIZATION_ID, organizationId.toString());

        OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder = Mockito.spy(new OnlineSchemaChangeContextHolder());
        SecurityContextUtils securityContextUtils = mock(SecurityContextUtils.class);
        when(securityContextUtils.setCurrentUser(userId, organizationId, null)).thenReturn(expectedContext);
        when(onlineSchemaChangeChangeContextHolder.getSecurityContextUtils()).thenReturn(securityContextUtils);

        // When
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        // Then
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        assertEquals(expectedContext, mdcContext);
        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));

        // Clean up
        OnlineSchemaChangeContextHolder.clear();
    }
}