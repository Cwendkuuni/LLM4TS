package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;
    private static final Long userId = 1L;
    private static final Long taskId = 2L;
    private static final Long organizationId = 3L;
    private static final String userIdStr = "1";
    private static final String taskIdStr = "2";
    private static final String organizationIdStr = "3";

    @Before
    public void setup() {
        onlineSchemaChangeContextHolder = OnlineSchemaChangeChangeContextHolder.getInstance();
    }

    @Test
    public void testTraceWithLongValues() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> expectedContext = new HashMap<>();
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, userId + "");
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, taskId + "");
        expectedContext.put(OdcConstants.ORGANIZATION_ID, organizationId + "");
        SecurityContextUtils securityContextUtils = Mockito.mock(SecurityContextUtils.class);
        Mockito.when(securityContextUtils.getCurrentUser(userId, organizationId, null)).thenReturn(null);
        OnlineSchemaChangeContextHolder.setSecurityContextUtils(securityContextUtils);
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        Map<String, String> actualContext = OnlineSchemaChangeContextHolder.getContext();
        assertEquals(expectedContext, actualContext);
    }

    @Test
    public void testTraceWithStringValues() {
        OnlineSchemaChangeContextHolder.trace(userIdStr, taskIdStr, organizationIdStr);
        Map<String, String> expectedContext = new HashMap<>();
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, userIdStr);
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userIdStr);
        expectedContext.put(OdcConstants.ORGANIZATION_ID, organizationIdStr);
        SecurityContextUtils securityContextUtils = Mockito.mock(SecurityContextUtils.class);
        Mockito.when(securityContextUtils.getCurrentUser(userId, organizationId, null)).thenReturn(null);
        OnlineSchemaChangeContextHolder.setSecurityContextUtils(securityContextUtils);
        OnlineSchemaChangeContextHolder.trace(userIdStr, taskIdStr, organizationIdStr);
        Map<String, String> actualContext = OnlineSchemaChangeContextHolder.getContext();
        assertEquals(expectedContext, actualContext);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "4");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "5");
        context.put(OdcConstants.ORGANIZATION_ID, "6");
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.retrace(context);
        Map<String, String> expectedContext = new HashMap<>();
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, "4");
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "5");
        expectedContext.put(OdcConstants.ORGANIZATION_ID, "6");
        assertEquals(expectedContext, OnlineSchemaChangeContextHolder.getContext());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSecurityContextUtilsNull() {
        OnlineSchemaChangeContextHolder.setSecurityContextUtils(null);
    }
}